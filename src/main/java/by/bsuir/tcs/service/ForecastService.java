package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.forecast.ForecastRequest;
import by.bsuir.tcs.dto.forecast.ForecastResponse;
import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.repository.CalculationResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForecastService {

    private final CalculationResultRepository calculationResultRepository;
    private final RestClient forecastRestClient;

    public ForecastResponse forecastForTmc(Long tmcId, Integer steps) {
        List<CalculationResult> historyData = calculationResultRepository.findHistoryByTmcId(tmcId);

        if (historyData == null || historyData.isEmpty()) {
            throw new IllegalStateException("Нет исторических данных для прогнозирования");
        }

        Map<String, Double> groupedByMonth = new TreeMap<>();
        for (CalculationResult res : historyData) {
            String key = String.format("%04d-%02d", res.getPeriodYear(), res.getPeriodMonth());
            groupedByMonth.merge(key, res.getRequiredQuantity().doubleValue(), Double::sum);
        }

        List<Double> history = new ArrayList<>(groupedByMonth.values());

        if (history.size() < 12) {
            log.warn("TMC {}: too little history ({} points)", tmcId, history.size());
        }

        ForecastRequest request = ForecastRequest.builder()
                .history(history)
                .steps(steps)
                .build();

        try {
            return forecastRestClient.post()
                    .uri("/forecast")
                    .body(request)
                    .retrieve()
                    .body(ForecastResponse.class);
        } catch (Exception e) {
            log.error("Forecast service call failed for tmcId={}", tmcId, e);
            throw new RuntimeException(
                    "Ошибка связи с сервисом прогнозирования. Убедитесь, что Python-сервис запущен. "
                            + e.getMessage(), e);
        }
    }
}