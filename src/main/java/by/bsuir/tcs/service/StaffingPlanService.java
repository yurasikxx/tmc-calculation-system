package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.StaffingPlanRequest;
import by.bsuir.tcs.entity.Department;
import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.entity.StaffingPlan;
import by.bsuir.tcs.repository.StaffingPlanRepository;
import lombok.RequiredArgsConstructor;
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
        for (StaffingPlanRequest request : requests) {
            Employee employee = findOrCreateEmployee(request);

            StaffingPlan plan = StaffingPlan.builder()
                    .employee(employee)
                    .actionType(request.getActionType())
                    .effectiveDate(request.getEffectiveDate())
                    .build();

            if ("TRANSFER".equals(request.getActionType())) {
                if (request.getNewProfessionName() != null) {
                    Profession newProfession = professionService.findAll().stream()
                            .filter(p -> p.getName().equals(request.getNewProfessionName()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Profession not found: " + request.getNewProfessionName()));
                    plan.setNewProfession(newProfession);
                }

                if (request.getNewDepartmentName() != null) {
                    Department newDepartment = departmentService.findAll().stream()
                            .filter(d -> d.getName().equals(request.getNewDepartmentName()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Department not found: " + request.getNewDepartmentName()));
                    plan.setNewDepartment(newDepartment);
                }
            }

            savedPlans.add(staffingPlanRepository.save(plan));
        }

        return savedPlans;
    }

    private Employee findOrCreateEmployee(StaffingPlanRequest request) {
        List<Employee> employees = employeeService.findAll();
        Employee employee = employees.stream()
                .filter(e -> e.getFullName().equals(request.getFullName()))
                .findFirst()
                .orElse(null);

        if (employee == null) {
            Profession profession = professionService.findAll().stream()
                    .filter(p -> p.getName().equals(request.getProfessionName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Profession not found: " + request.getProfessionName()));

            Department department = departmentService.findAll().stream()
                    .filter(d -> d.getName().equals(request.getDepartmentName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Department not found: " + request.getDepartmentName()));

            employee = Employee.builder()
                    .fullName(request.getFullName())
                    .profession(profession)
                    .department(department)
                    .hireDate(LocalDate.now())
                    .build();

            employee = employeeService.create(employee);
        }

        return employee;
    }

    @Transactional(readOnly = true)
    public List<StaffingPlan> findByPeriod(LocalDate start, LocalDate end) {
        return staffingPlanRepository.findByEffectiveDateBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<StaffingPlan> findAll() {
        return staffingPlanRepository.findAll();
    }
}