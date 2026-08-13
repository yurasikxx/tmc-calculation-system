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
            StaffingPlan plan = createStaffingPlan(employee, request);
            savedPlans.add(staffingPlanRepository.save(plan));
        }

        return savedPlans;
    }

    private Employee findOrCreateEmployee(StaffingPlanRequest request) {
        Employee employee = employeeService.findByFullName(request.getFullName());

        if (employee == null) {
            Profession profession = getProfessionByName(request.getProfessionName());
            Department department = getDepartmentByName(request.getDepartmentName());
            employee = createNewEmployee(request, profession, department);
        }

        return employee;
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
            handleTransfer(employee, request, builder);
        } else if ("TERMINATE".equals(request.getActionType())) {
            employee.setTerminationDate(request.getEffectiveDate());
            employeeService.update(employee.getId(), employee);
        }

        return builder.build();
    }

    private void handleTransfer(Employee employee, StaffingPlanRequest request, StaffingPlan.StaffingPlanBuilder builder) {
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
    public List<StaffingPlan> findByPeriod(LocalDate start, LocalDate end) {
        return staffingPlanRepository.findByEffectiveDateBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<StaffingPlan> findAll() {
        return staffingPlanRepository.findAll();
    }
}