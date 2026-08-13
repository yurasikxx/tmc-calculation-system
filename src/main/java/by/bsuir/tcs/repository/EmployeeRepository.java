package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByFullName(String fullName);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByProfessionId(Long professionId);

    Page<Employee> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
}