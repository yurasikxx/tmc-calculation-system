package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.Department;
import by.bsuir.tcs.repository.DepartmentRepository;
import by.bsuir.tcs.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Department> findBySearch(String search, Pageable pageable) {
        return departmentRepository.findByNameContainingIgnoreCase(search, pageable);
    }

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Transactional
    public Department create(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department with name '" + department.getName() + "' already exists");
        }
        return departmentRepository.save(department);
    }

    @Transactional
    public Department update(Long id, Department updatedDepartment) {
        Department existing = findById(id);
        if (!existing.getName().equals(updatedDepartment.getName())
                && departmentRepository.existsByName(updatedDepartment.getName())) {
            throw new RuntimeException("Department with name '" + updatedDepartment.getName() + "' already exists");
        }
        existing.setName(updatedDepartment.getName());
        return departmentRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Department department = findById(id);
        if (!employeeRepository.findByDepartmentId(id).isEmpty()) {
            throw new RuntimeException("Cannot delete department with id " + id + " because it has employees");
        }
        departmentRepository.delete(department);
    }
}