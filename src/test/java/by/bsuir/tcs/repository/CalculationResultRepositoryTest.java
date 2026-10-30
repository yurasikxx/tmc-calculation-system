package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.entity.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
class CalculationResultRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb").withUsername("test").withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private CalculationResultRepository repository;
    @Autowired
    private TestEntityManager em;

    private TmcItem tmc;

    @BeforeEach
    void setUp() {
        TmcType type = em.persist(TmcType.builder().name("SIZ").build());
        Unit unit = em.persist(Unit.builder().code("шт").name("Штука").build());
        tmc = em.persist(TmcItem.builder()
                .code("SIZ-001").name("Перчатки").type(type).unit(unit)
                .serviceLifeMonths(6).build());
    }

    @Test
    void findByPeriodYearAndPeriodMonth_returnsOnlyPeriod() {
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(10).source("NORM_BASE").requiredQuantity(5).build());
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(11).source("NORM_BASE").requiredQuantity(7).build());

        List<CalculationResult> result = repository.findByPeriodYearAndPeriodMonth(2026, 10);

        assertEquals(1, result.size());
        assertEquals(5, result.getFirst().getRequiredQuantity());
    }

    @Test
    void findByPeriodYearAndPeriodMonthAndSourceAndTmcItemId_found() {
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(10).source("HIRE").requiredQuantity(3).build());

        var result = repository.findByPeriodYearAndPeriodMonthAndSourceAndTmcItemId(
                2026, 10, "HIRE", tmc.getId());

        assertTrue(result.isPresent());
        assertEquals(3, result.get().getRequiredQuantity());
    }

    @Test
    void findHistoryByTmcId_orderedByPeriod() {
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(11).source("NORM_BASE").requiredQuantity(2).build());
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(10).source("NORM_BASE").requiredQuantity(1).build());

        List<CalculationResult> history = repository.findHistoryByTmcId(tmc.getId());

        assertEquals(2, history.size());
        assertEquals(10, history.get(0).getPeriodMonth());
        assertEquals(11, history.get(1).getPeriodMonth());
    }

    @Test
    void deleteByPeriodYearAndPeriodMonth_removesOnlyPeriod() {
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(10).source("NORM_BASE").requiredQuantity(5).build());
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(11).source("NORM_BASE").requiredQuantity(7).build());

        repository.deleteByPeriodYearAndPeriodMonth(2026, 10);

        assertTrue(repository.findByPeriodYearAndPeriodMonth(2026, 10).isEmpty());
        assertEquals(1, repository.findByPeriodYearAndPeriodMonth(2026, 11).size());
    }

    @Test
    void countByMonth_groupsCorrectly() {
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(10).source("NORM_BASE").requiredQuantity(5).build());
        repository.save(CalculationResult.builder().tmcItem(tmc)
                .periodYear(2026).periodMonth(10).source("HIRE").requiredQuantity(3).build());

        List<Object[]> counts = repository.countByMonth();

        assertEquals(1, counts.size());
        assertEquals(2L, counts.getFirst()[2]);
    }
}