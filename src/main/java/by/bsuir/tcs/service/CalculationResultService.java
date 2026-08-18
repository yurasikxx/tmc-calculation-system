package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.repository.CalculationResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculationResultService {

    private final CalculationResultRepository calculationResultRepository;

    @Transactional
    public CalculationResult save(CalculationResult result) {
        return calculationResultRepository.save(result);
    }

    @Transactional(readOnly = true)
    public List<CalculationResult> findByPeriod(Integer year, Integer month) {
        return calculationResultRepository.findByPeriodYearAndPeriodMonth(year, month);
    }

    @Transactional(readOnly = true)
    public Page<CalculationResult> findByPeriod(Integer year, Integer month, Pageable pageable) {
        return calculationResultRepository.findByPeriodYearAndPeriodMonth(year, month, pageable);
    }

    @Transactional
    public void clearByPeriod(Integer year, Integer month) {
        calculationResultRepository.deleteByPeriodYearAndPeriodMonth(year, month);
    }

    @Transactional(readOnly = true)
    public List<CalculationResult> findAll() {
        return calculationResultRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CalculationResult> findAll(Pageable pageable) {
        return calculationResultRepository.findAll(pageable);
    }

    @Transactional
    public void delete(Long id) {
        calculationResultRepository.deleteById(id);
    }
}