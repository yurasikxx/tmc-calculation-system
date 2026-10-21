package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.CalculationResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalculationResultRepository extends JpaRepository<CalculationResult, Long>,
        JpaSpecificationExecutor<CalculationResult> {

    List<CalculationResult> findByPeriodYearAndPeriodMonth(Integer year, Integer month);

    Page<CalculationResult> findByPeriodYearAndPeriodMonth(Integer year, Integer month, Pageable pageable);

    Optional<CalculationResult> findByPeriodYearAndPeriodMonthAndSourceAndTmcItemId(
            Integer year, Integer month, String source, Long tmcId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CalculationResult c WHERE c.periodYear = :year AND c.periodMonth = :month")
    void deleteByPeriodYearAndPeriodMonth(
            @Param("year") Integer year, @Param("month") Integer month);

    List<CalculationResult> findTop5ByOrderByCalculationDateDesc();

    @Query("""
            SELECT c.periodYear, c.periodMonth, COUNT(c)
            FROM CalculationResult c
            GROUP BY c.periodYear, c.periodMonth
            ORDER BY c.periodYear, c.periodMonth
            """)
    List<Object[]> countByMonth();

    @Query("""
            SELECT c FROM CalculationResult c
            WHERE c.tmcItem.id = :tmcId
            ORDER BY c.periodYear ASC, c.periodMonth ASC
            """)
    List<CalculationResult> findHistoryByTmcId(@Param("tmcId") Long tmcId);
}