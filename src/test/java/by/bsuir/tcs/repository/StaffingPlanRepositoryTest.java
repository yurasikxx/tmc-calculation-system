package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.Department;
import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.entity.StaffingPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
class StaffingPlanRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb").withUsername("test").withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.liquibase.enabled", () -> "false");
    }

    @Autowired
    private StaffingPlanRepository repository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProfessionRepository professionRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        employeeRepository.deleteAll();
        Profession prof = professionRepository.save(Profession.builder().name("Токарь").build());
        Department dept = departmentRepository.save(Department.builder().name("Цех 1").build());
        employee = employeeRepository.save(Employee.builder()
                .fullName("Иванов Иван").hireDate(LocalDate.now().minusYears(1))
                .profession(prof).department(dept).build());
    }

    private StaffingPlan plan(String action, LocalDate date) {
        return StaffingPlan.builder().employee(employee).actionType(action).effectiveDate(date)
                .newProfession(employee.getProfession()).newDepartment(employee.getDepartment()).build();
    }

    @Test
    void findByEffectiveDateBetween_returnsOnlyInRange() {
        repository.saveAll(List.of(
                plan("HIRE", LocalDate.of(2026, 10, 1)),
                plan("TERMINATE", LocalDate.of(2026, 11, 15))
        ));
        Page<StaffingPlan> page = repository.findByEffectiveDateBetween(
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31), PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals("HIRE", page.getContent().getFirst().getActionType());
    }

    @Test
    void findByActionType_returnsOnlyMatching() {
        repository.saveAll(List.of(plan("HIRE", LocalDate.now()), plan("TERMINATE", LocalDate.now())));
        Page<StaffingPlan> page = repository.findByActionType("TERMINATE", PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void findByEmployeeId_returnsAllRecordsForEmployee() {
        repository.saveAll(List.of(plan("HIRE", LocalDate.now()), plan("TRANSFER", LocalDate.now().plusMonths(1))));
        List<StaffingPlan> found = repository.findByEmployeeId(employee.getId());
        assertEquals(2, found.size());
    }
}