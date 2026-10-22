package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.ReportItemDto;
import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.entity.TmcItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

        Map<Long, Map<String, Integer>> byTmc = new LinkedHashMap<>();
        for (CalculationResult r : results) {
            Long tmcId = r.getTmcItem().getId();
            String source = r.getSource() == null ? "NORM_BASE" : r.getSource();
            byTmc.computeIfAbsent(tmcId, k -> new LinkedHashMap<>())
                    .merge(source, r.getRequiredQuantity(), Integer::sum);
        }

        List<ReportItemDto> out = new ArrayList<>();
        for (Map.Entry<Long, Map<String, Integer>> e : byTmc.entrySet()) {
            TmcItem tmc = tmcItemService.findById(e.getKey());
            Map<String, Integer> src = e.getValue();

            int normBase = src.getOrDefault("NORM_BASE", 0);
            int hire = src.getOrDefault("HIRE", 0);
            int replacement = src.getOrDefault("REPLACEMENT", 0);

            out.add(ReportItemDto.builder()
                    .tmcCode(tmc.getCode())
                    .tmcName(tmc.getName())
                    .tmcType(tmc.getType().getName())
                    .unit(tmc.getUnit().getCode())
                    .normBaseQuantity(normBase)
                    .hireQuantity(hire)
                    .replacementQuantity(replacement)
                    .totalQuantity(normBase + hire + replacement)
                    .build());
        }
        return out;
    }

    @Transactional(readOnly = true)
    public Map<String, List<ReportItemDto>> groupByTmcType(Integer year, Integer month) {
        return getReportByPeriod(year, month).stream()
                .collect(Collectors.groupingBy(
                        ReportItemDto::getTmcType,
                        LinkedHashMap::new,
                        Collectors.toList()));
    }
}