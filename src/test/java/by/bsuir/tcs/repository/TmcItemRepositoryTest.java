package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.entity.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class TmcItemRepositoryTest {

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
    private TmcItemRepository repository;
    @Autowired
    private TmcTypeRepository tmcTypeRepository;
    @Autowired
    private UnitRepository unitRepository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        Unit unit = unitRepository.save(Unit.builder().code("шт").name("Штука").build());
        TmcType sizType = tmcTypeRepository.save(TmcType.builder().name("SIZ").build());
        TmcType toolType = tmcTypeRepository.save(TmcType.builder().name("TOOL").build());
        repository.saveAll(List.of(
                TmcItem.builder().code("SIZ-001").name("Перчатки").type(sizType).unit(unit).serviceLifeMonths(6).build(),
                TmcItem.builder().code("TOOL-001").name("Штангенциркуль").type(toolType).unit(unit).serviceLifeMonths(24).build()
        ));
    }

    @Test
    void findByCode_returnsItem() {
        assertTrue(repository.findByCode("SIZ-001").isPresent());
        assertFalse(repository.findByCode("NOPE").isPresent());
    }

    @Test
    void findByTypeName_returnsOnlyThatType() {
        List<TmcItem> tools = repository.findByTypeName("TOOL");
        assertEquals(1, tools.size());
        assertEquals("Штангенциркуль", tools.getFirst().getName());
    }

    @Test
    void findByNameContainingIgnoreCase_findsByPartOfName() {
        List<TmcItem> found = repository.findByNameContainingIgnoreCase("перч");
        assertEquals(1, found.size());
    }
}