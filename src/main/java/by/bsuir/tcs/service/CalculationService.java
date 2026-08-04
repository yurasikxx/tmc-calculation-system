package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.*;
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
            if (!plan.getActionType().equals("HIRE") && !plan.getActionType().equals("TRANSFER")) {
                continue;
            }

            Profession profession = plan.getActionType().equals("HIRE")
                    ? plan.getEmployee().getProfession()
                    : plan.getNewProfession();

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