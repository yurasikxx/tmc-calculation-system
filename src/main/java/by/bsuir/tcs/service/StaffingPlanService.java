package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.StaffingPlanRequest;
import by.bsuir.tcs.entity.Department;
import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.entity.StaffingPlan;
import by.bsuir.tcs.repository.StaffingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffingPlanService {

    private final StaffingPlanRepository staffingPlanRepository;
    private final EmployeeService employeeService;
    private final ProfessionService professionService;
    private final DepartmentService departmentService;

    @Transactional
    public List<StaffingPlan> importPlan(List<StaffingPlanRequest> requests) {
        List<StaffingPlan> savedPlans = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        for (StaffingPlanRequest request : requests) {
            if (!isProductionProfession(request.getProfessionName())) {
                skipped.add(request.getFullName() + " (не производственная профессия: " + request.getProfessionName() + ")");
                continue;
            }

            Employee employee = employeeService.findByFullName(request.getFullName());

            if (employee != null && employee.getTerminationDate() != null) {
                skipped.add(request.getFullName() + " (уволен " + employee.getTerminationDate() + ")");
                continue;
            }

            if (employee != null && "HIRE".equals(request.getActionType())) {
                skipped.add(request.getFullName() + " (уже работает с " + employee.getHireDate() + ")");
                continue;
            }

            if (employee == null && "HIRE".equals(request.getActionType())) {
                Profession profession = getProfessionByName(request.getProfessionName());
                Department department = getDepartmentByName(request.getDepartmentName());
                employee = createNewEmployee(request, profession, department);
            }

            if (employee == null && !"HIRE".equals(request.getActionType())) {
                throw new RuntimeException("Сотрудник " + request.getFullName() + " не найден для действия " + request.getActionType());
            }

            StaffingPlan plan = createStaffingPlan(employee, request);
            savedPlans.add(staffingPlanRepository.save(plan));
        }

        if (!skipped.isEmpty()) {
            System.out.println("Пропущено событий: " + String.join("; ", skipped));
        }

        return savedPlans;
    }

    private boolean isProductionProfession(String professionName) {
        List<String> nonProductionProfessions = List.of(
                "Инженер-технолог",
                "Инженер по охране труда",
                "Кладовщик ИРК",
                "Инженер по МТС",
                "Инженер по организации и нормированию труда"
        );
        return nonProductionProfessions.stream().noneMatch(professionName::contains);
    }

    private Employee createNewEmployee(StaffingPlanRequest request, Profession profession, Department department) {
        Employee employee = Employee.builder()
                .fullName(request.getFullName())
                .profession(profession)
                .department(department)
                .hireDate(request.getEffectiveDate())
                .build();
        return employeeService.create(employee);
    }

    private StaffingPlan createStaffingPlan(Employee employee, StaffingPlanRequest request) {
        StaffingPlan.StaffingPlanBuilder builder = StaffingPlan.builder()
                .employee(employee)
                .actionType(request.getActionType())
                .effectiveDate(request.getEffectiveDate());

        if ("HIRE".equals(request.getActionType())) {
            builder.newProfession(employee.getProfession());
            builder.newDepartment(employee.getDepartment());
        } else if ("TRANSFER".equals(request.getActionType())) {
            // Сохраняем старые значения до обновления
            builder.oldProfession(employee.getProfession());
            builder.oldDepartment(employee.getDepartment());

            Profession newProfession = null;
            Department newDepartment = null;

            if (request.getNewProfessionName() != null && !request.getNewProfessionName().isEmpty()) {
                newProfession = getProfessionByName(request.getNewProfessionName());
                employee.setProfession(newProfession);
            }

            if (request.getNewDepartmentName() != null && !request.getNewDepartmentName().isEmpty()) {
                newDepartment = getDepartmentByName(request.getNewDepartmentName());
                employee.setDepartment(newDepartment);
            }

            if (newProfession != null || newDepartment != null) {
                employeeService.update(employee.getId(), employee);
                builder.newProfession(newProfession);
                builder.newDepartment(newDepartment);
            }
        } else if ("TERMINATE".equals(request.getActionType())) {
            if (employee.getTerminationDate() != null) {
                throw new RuntimeException("Сотрудник " + employee.getFullName() + " уже уволен " + employee.getTerminationDate());
            }
            employee.setTerminationDate(request.getEffectiveDate());
            employeeService.update(employee.getId(), employee);
        }

        return builder.build();
    }

    private Profession getProfessionByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return professionService.findAll().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Profession not found: " + name));
    }

    private Department getDepartmentByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return departmentService.findAll().stream()
                .filter(d -> d.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Department not found: " + name));
    }

    @Transactional(readOnly = true)
    public Page<StaffingPlan> findAll(Pageable pageable) {
        return staffingPlanRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<StaffingPlan> findByPeriod(LocalDate start, LocalDate end, Pageable pageable) {
        return staffingPlanRepository.findByEffectiveDateBetween(start, end, pageable);
    }

    @Transactional(readOnly = true)
    public Page<StaffingPlan> findByActionType(String actionType, Pageable pageable) {
        return staffingPlanRepository.findByActionType(actionType, pageable);
    }

    @Transactional(readOnly = true)
    public List<StaffingPlan> findAll() {
        return staffingPlanRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<StaffingPlan> findByPeriod(LocalDate start, LocalDate end) {
        return staffingPlanRepository.findByEffectiveDateBetween(start, end);
    }

    @Transactional(readOnly = true)
    public StaffingPlan findById(Long id) {
        return staffingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись плана не найдена"));
    }

    @Transactional(readOnly = true)
    public List<StaffingPlan> findByEmployee(Long employeeId) {
        return staffingPlanRepository.findByEmployeeId(employeeId);
    }

    @Transactional
    public void delete(Long id) {
        staffingPlanRepository.deleteById(id);
    }
}