package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.DashboardStatsDto;
import by.bsuir.tcs.dto.RecentCalculationDto;
import by.bsuir.tcs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DepartmentRepository departmentRepository;
    private final ProfessionRepository professionRepository;
    private final TmcItemRepository tmcItemRepository;
    private final NormRepository normRepository;
    private final CalculationResultRepository calculationResultRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getStats() {
        return DashboardStatsDto.builder()
                .departmentsCount(departmentRepository.count())
                .professionsCount(professionRepository.count())
                .tmcItemsCount(tmcItemRepository.count())
                .normsCount(normRepository.count())
                .calculationsCount(calculationResultRepository.count())
                .build();
    }

    @Transactional(readOnly = true)
    public List<RecentCalculationDto> getRecentCalculations() {
        // Берём последние 5 расчётов (группируем по уникальным периодам)
        return calculationResultRepository.findAll().stream()
                .map(result -> RecentCalculationDto.builder()
                        .period(result.getPeriodMonth() + "/" + result.getPeriodYear())
                        .tmcName(result.getTmcItem().getName())
                        .requiredQuantity(result.getRequiredQuantity())
                        .calculationDate(result.getCalculationDate())
                        .build())
                .limit(5)
                .collect(Collectors.toList());
    }
}