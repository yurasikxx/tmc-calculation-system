package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.CalculationResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculationResultRepository extends JpaRepository<CalculationResult, Long> {
    List<CalculationResult> findByPeriodYearAndPeriodMonth(Integer year, Integer month);

    Page<CalculationResult> findByPeriodYearAndPeriodMonth(Integer year, Integer month, Pageable pageable);

    void deleteByPeriodYearAndPeriodMonth(Integer year, Integer month);

    List<CalculationResult> findTop5ByOrderByCalculationDateDesc();
}