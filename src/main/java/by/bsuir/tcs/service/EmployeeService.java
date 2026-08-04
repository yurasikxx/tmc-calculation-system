package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.Department;
import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final ProfessionService professionService;

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Transactional
    public Employee create(Employee employee) {
        if (employee.getProfession() == null || employee.getProfession().getId() == null) {
            throw new RuntimeException("Profession must be specified");
        }

        if (employee.getDepartment() == null || employee.getDepartment().getId() == null) {
            throw new RuntimeException("Department must be specified");
        }

        Profession profession = professionService.findById(employee.getProfession().getId());
        Department department = departmentService.findById(employee.getDepartment().getId());

        employee.setProfession(profession);
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(Long id, Employee updatedEmployee) {
        Employee existing = findById(id);

        if (updatedEmployee.getFullName() != null) {
            existing.setFullName(updatedEmployee.getFullName());
        }

        if (updatedEmployee.getHireDate() != null) {
            existing.setHireDate(updatedEmployee.getHireDate());
        }

        if (updatedEmployee.getTerminationDate() != null) {
            existing.setTerminationDate(updatedEmployee.getTerminationDate());
        }

        if (updatedEmployee.getProfession() != null && updatedEmployee.getProfession().getId() != null) {
            Profession profession = professionService.findById(updatedEmployee.getProfession().getId());
            existing.setProfession(profession);
        }

        if (updatedEmployee.getDepartment() != null && updatedEmployee.getDepartment().getId() != null) {
            Department department = departmentService.findById(updatedEmployee.getDepartment().getId());
            existing.setDepartment(department);
        }

        return employeeRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByProfession(Long professionId) {
        return employeeRepository.findByProfessionId(professionId);
    }
}