package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.*;
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
class IssuedItemRepositoryTest {

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
    private IssuedItemRepository repository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProfessionRepository professionRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TmcItemRepository tmcItemRepository;
    @Autowired
    private TmcTypeRepository tmcTypeRepository;
    @Autowired
    private UnitRepository unitRepository;

    private Employee employee;
    private TmcItem tmc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        employeeRepository.deleteAll();
        Profession profession = professionRepository.save(Profession.builder().name("Токарь").build());
        Department dept = departmentRepository.save(Department.builder().name("Цех 1").build());
        employee = employeeRepository.save(Employee.builder()
                .fullName("Иванов Иван Иванович").hireDate(LocalDate.now().minusYears(1))
                .profession(profession).department(dept).build());
        Unit unit = unitRepository.save(Unit.builder().code("шт").name("Штука").build());
        TmcType type = tmcTypeRepository.save(TmcType.builder().name("SIZ").build());
        tmc = tmcItemRepository.save(TmcItem.builder()
                .code("SIZ-001").name("Перчатки").type(type).unit(unit).serviceLifeMonths(6).build());
    }

    private IssuedItem newIssued(String status, String tmcName) {
        TmcItem item = tmc;
        if (!tmc.getName().equals(tmcName)) {
            item = tmcItemRepository.save(TmcItem.builder()
                    .code("SIZ-002").name(tmcName).type(tmc.getType()).unit(tmc.getUnit())
                    .serviceLifeMonths(6).build());
        }
        return IssuedItem.builder().employee(employee).tmcItem(item)
                .quantity(1).issueDate(LocalDate.now()).expectedReturnDate(LocalDate.now().plusMonths(6))
                .status(status).build();
    }

    @Test
    void findByEmployeeId_returnsOnlyThatEmployee() {
        repository.save(newIssued("ISSUED", "Перчатки"));
        List<IssuedItem> found = repository.findByEmployeeId(employee.getId());
        assertEquals(1, found.size());
        assertEquals(employee.getId(), found.getFirst().getEmployee().getId());
    }

    @Test
    void findAllFiltered_byStatus_returnsOnlyMatching() {
        repository.saveAll(List.of(newIssued("ISSUED", "Перчатки"), newIssued("RETURNED", "Каска")));
        Page<IssuedItem> page = repository.findAllFiltered("RETURNED", null, null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals("RETURNED", page.getContent().getFirst().getStatus());
    }

    @Test
    void findAllFiltered_bySearch_findsByEmployeeOrTmcName() {
        repository.save(newIssued("ISSUED", "Перчатки"));
        Page<IssuedItem> page = repository.findAllFiltered(null, null, "%иван%", PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void findActiveByDate_returnsOnlyIssuedWithFutureReturnDate() {
        repository.save(newIssued("ISSUED", "Перчатки"));
        repository.save(newIssued("RETURNED", "Каска"));
        List<IssuedItem> active = repository.findActiveByDate(LocalDate.now());
        assertEquals(1, active.size());
        assertEquals("ISSUED", active.getFirst().getStatus());
    }
}