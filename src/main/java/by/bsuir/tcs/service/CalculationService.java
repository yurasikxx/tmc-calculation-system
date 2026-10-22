package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.*;
import by.bsuir.tcs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final StaffingPlanRepository staffingPlanRepository;
    private final NormRepository normRepository;
    private final EmployeeRepository employeeRepository;
    private final IssuedItemRepository issuedItemRepository;
    private final CalculationResultRepository calculationResultRepository;
    private final CalculationResultService calculationResultService;
    private final TmcItemService tmcItemService;

    @Transactional
    public void calculate(Integer year, Integer month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        calculationResultService.clearByPeriod(year, month);

        Map<Long, Integer> employeesByProfession = employeeRepository.findAll().stream()
                .filter(e -> !e.getHireDate().isAfter(end)
                        && (e.getTerminationDate() == null
                        || !e.getTerminationDate().isBefore(start)))
                .collect(Collectors.groupingBy(
                        e -> e.getProfession().getId(),
                        Collectors.summingInt(e -> 1)));

        Map<Long, Integer> openIssued = new HashMap<>();
        for (IssuedItem ii : issuedItemRepository.findActiveByDate(end)) {
            openIssued.merge(ii.getTmcItem().getId(), ii.getQuantity(), Integer::sum);
        }

        Map<Long, double[]> targets = new HashMap<>();
        for (Norm norm : normRepository.findAll()) {
            int cnt = employeesByProfession.getOrDefault(norm.getProfession().getId(), 0);
            if (cnt == 0) continue;

            double targetTotal = (double) norm.getQuantity() * cnt;
            double[] arr = targets.computeIfAbsent(norm.getTmcItem().getId(),
                    k -> new double[]{0, 0});
            arr[0] += targetTotal;
            arr[1] = Math.max(arr[1], norm.getPeriodMonths());
        }

        Map<Long, Double> normBase = new HashMap<>();
        for (Map.Entry<Long, double[]> e : targets.entrySet()) {
            Long tmcId = e.getKey();
            double targetTotal = e.getValue()[0];
            double periodMonths = e.getValue()[1] > 0 ? e.getValue()[1] : 12;

            int open = openIssued.getOrDefault(tmcId, 0);
            double missing = Math.max(0, targetTotal - open);
            if (missing > 0) {
                normBase.put(tmcId, missing / periodMonths);
            }
        }

        List<StaffingPlan> plans = staffingPlanRepository.findByEffectiveDateBetween(start, end);

        Map<Long, Integer> hire = new HashMap<>();
        for (StaffingPlan plan : plans) {
            Profession profession = getProfessionForHire(plan);
            if (profession == null) continue;
            for (Norm norm : normRepository.findByProfessionId(profession.getId())) {
                hire.merge(norm.getTmcItem().getId(), norm.getQuantity(), Integer::sum);
            }
        }

        List<IssuedItem> expiring = issuedItemRepository.findExpiringBetween(start, end.plusDays(1));

        Map<Long, Integer> replacement = expiring.stream()
                .collect(Collectors.groupingBy(
                        i -> i.getTmcItem().getId(),
                        Collectors.summingInt(IssuedItem::getQuantity)));

        saveResultsDouble(normBase, "NORM_BASE", year, month);
        saveResultsInt(hire, "HIRE", year, month);
        saveResultsInt(replacement, "REPLACEMENT", year, month);
    }

    private Profession getProfessionForHire(StaffingPlan plan) {
        return switch (plan.getActionType()) {
            case "HIRE", "TRANSFER" -> plan.getNewProfession();
            default -> null;
        };
    }

    private void saveResultsDouble(Map<Long, Double> data, String source,
                                   Integer year, Integer month) {
        for (Map.Entry<Long, Double> e : data.entrySet()) {
            int qty = (int) Math.round(e.getValue());
            if (qty <= 0) continue;
            saveResult(e.getKey(), qty, source, year, month);
        }
    }

    private void saveResultsInt(Map<Long, Integer> data, String source,
                                Integer year, Integer month) {
        for (Map.Entry<Long, Integer> e : data.entrySet()) {
            if (e.getValue() == null || e.getValue() <= 0) continue;
            saveResult(e.getKey(), e.getValue(), source, year, month);
        }
    }

    private void saveResult(Long tmcId, int qty, String source,
                            Integer year, Integer month) {
        TmcItem tmcItem = tmcItemService.findById(tmcId);

        Optional<CalculationResult> existing =
                calculationResultRepository
                        .findByPeriodYearAndPeriodMonthAndSourceAndTmcItemId(
                                year, month, source, tmcId);

        CalculationResult result = existing.orElseGet(() ->
                CalculationResult.builder()
                        .tmcItem(tmcItem)
                        .periodMonth(month)
                        .periodYear(year)
                        .source(source)
                        .build());

        result.setRequiredQuantity(qty);
        calculationResultService.save(result);
    }
}