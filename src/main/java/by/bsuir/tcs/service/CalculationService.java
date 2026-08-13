package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.entity.StaffingPlan;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.repository.NormRepository;
import by.bsuir.tcs.repository.StaffingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final StaffingPlanRepository staffingPlanRepository;
    private final NormRepository normRepository;
    private final CalculationResultService calculationResultService;
    private final TmcItemService tmcItemService;

    @Transactional
    public void calculate(Integer year, Integer month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        calculationResultService.clearByPeriod(year, month);

        List<StaffingPlan> plans = staffingPlanRepository.findByEffectiveDateBetween(start, end);

        Map<Long, Integer> aggregated = new HashMap<>();

        for (StaffingPlan plan : plans) {
            Profession profession = getProfessionForPlan(plan);

            if (profession == null) {
                continue;
            }

            List<Norm> norms = normRepository.findByProfessionId(profession.getId());

            if (norms.isEmpty()) {
                continue;
            }

            for (Norm norm : norms) {
                Long tmcId = norm.getTmcItem().getId();
                aggregated.put(tmcId, aggregated.getOrDefault(tmcId, 0) + norm.getQuantity());
            }
        }

        saveCalculationResults(aggregated, year, month);
    }

    private Profession getProfessionForPlan(StaffingPlan plan) {
        if ("TERMINATE".equals(plan.getActionType())) {
            return null;
        }

        if ("HIRE".equals(plan.getActionType())) {
            return plan.getNewProfession();
        }

        if ("TRANSFER".equals(plan.getActionType())) {
            return plan.getNewProfession();
        }

        return null;
    }

    private void saveCalculationResults(Map<Long, Integer> aggregated, Integer year, Integer month) {
        for (Map.Entry<Long, Integer> entry : aggregated.entrySet()) {
            Long tmcId = entry.getKey();
            Integer quantity = entry.getValue();

            TmcItem tmcItem = tmcItemService.findById(tmcId);

            CalculationResult result = CalculationResult.builder()
                    .tmcItem(tmcItem)
                    .requiredQuantity(quantity)
                    .periodMonth(month)
                    .periodYear(year)
                    .build();

            calculationResultService.save(result);
        }
    }
}