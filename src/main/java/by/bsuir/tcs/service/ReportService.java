package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.ReportItemDto;
import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.entity.TmcItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CalculationResultService calculationResultService;
    private final TmcItemService tmcItemService;

    @Transactional(readOnly = true)
    public List<ReportItemDto> getReportByPeriod(Integer year, Integer month) {
        List<CalculationResult> results = calculationResultService.findByPeriod(year, month);

        return results.stream()
                .map(result -> {
                    TmcItem tmc = tmcItemService.findById(result.getTmcItem().getId());
                    return ReportItemDto.builder()
                            .tmcCode(tmc.getCode())
                            .tmcName(tmc.getName())
                            .tmcType(tmc.getType().getName())
                            .unit(tmc.getUnit().getCode())
                            .requiredQuantity(result.getRequiredQuantity())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, List<ReportItemDto>> groupByTmcType(Integer year, Integer month) {
        List<ReportItemDto> items = getReportByPeriod(year, month);
        return items.stream()
                .collect(Collectors.groupingBy(ReportItemDto::getTmcType));
    }
}