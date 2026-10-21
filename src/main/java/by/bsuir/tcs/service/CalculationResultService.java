package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.repository.CalculationResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        List<CalculationResult> existing = calculationResultRepository.findByPeriodYearAndPeriodMonth(year, month);
        if (!existing.isEmpty()) {
            calculationResultRepository.deleteAllInBatch(existing);
        }
    }

    @Transactional(readOnly = true)
    public Page<CalculationResult> findFiltered(
            Integer year, Integer month, String source, Pageable pageable) {

        java.util.List<Specification<CalculationResult>> specs = new java.util.ArrayList<>();

        if (year != null) {
            specs.add((root, q, cb) -> cb.equal(root.get("periodYear"), year));
        }
        if (month != null) {
            specs.add((root, q, cb) -> cb.equal(root.get("periodMonth"), month));
        }
        if (source != null && !source.isBlank()) {
            specs.add((root, q, cb) -> cb.equal(root.get("source"), source));
        }

        Specification<CalculationResult> spec = Specification.allOf(specs);
        return calculationResultRepository.findAll(spec, pageable);
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