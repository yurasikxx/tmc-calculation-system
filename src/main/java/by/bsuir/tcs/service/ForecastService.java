package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.forecast.ForecastPoint;
import by.bsuir.tcs.dto.forecast.ForecastRequest;
import by.bsuir.tcs.dto.forecast.ForecastResponse;
import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.repository.CalculationResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForecastService {

    private static final String[] MONTHS_RU = {
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    };

    private static final String[] MONTHS_RU_SHORT = {
            "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
            "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
    };

    private final CalculationResultRepository calculationResultRepository;
    private final RestClient forecastRestClient;

    public ForecastResponse forecastForTmc(Long tmcId, Integer steps) {
        List<CalculationResult> historyData =
                calculationResultRepository.findHistoryByTmcId(tmcId);

        if (historyData == null || historyData.isEmpty()) {
            throw new IllegalStateException("Нет исторических данных для прогнозирования");
        }

        Map<String, Double> groupedByMonth = new TreeMap<>();
        for (CalculationResult res : historyData) {
            String key = String.format("%04d-%02d",
                    res.getPeriodYear(), res.getPeriodMonth());
            groupedByMonth.merge(key, res.getRequiredQuantity().doubleValue(), Double::sum);
        }

        List<Double> history = new ArrayList<>(groupedByMonth.values());

        if (history.size() < 12) {
            log.warn("TMC {}: too little history ({} points)", tmcId, history.size());
        }

        LocalDate startDate = nextMonthFromLastKey(groupedByMonth);

        ForecastRequest request = ForecastRequest.builder()
                .history(history)
                .steps(steps)
                .startDate(startDate)
                .build();

        try {
            ForecastResponse response = forecastRestClient.post()
                    .uri("/forecast")
                    .body(request)
                    .retrieve()
                    .body(ForecastResponse.class);

            if (response != null && response.getForecast() != null) {
                enrichLabels(response.getForecast());
            }
            return response;

        } catch (Exception e) {
            log.error("Forecast service call failed for tmcId={}", tmcId, e);
            throw new RuntimeException(
                    "Ошибка связи с сервисом прогнозирования. "
                            + e.getMessage(), e);
        }
    }

    private LocalDate nextMonthFromLastKey(Map<String, Double> grouped) {
        if (grouped.isEmpty()) {
            return LocalDate.now().withDayOfMonth(1).plusMonths(1);
        }
        String lastKey = ((TreeMap<String, Double>) grouped).lastKey();
        String[] parts = lastKey.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        YearMonth ym = YearMonth.of(year, month).plusMonths(1);
        return ym.atDay(1);
    }

    private void enrichLabels(List<ForecastPoint> points) {
        for (ForecastPoint p : points) {
            if (p.getPeriodYear() == null || p.getPeriodMonth() == null) {
                p.setLabel("Месяц " + p.getStep());
                p.setShortLabel("М" + p.getStep());
                continue;
            }
            String ru = MONTHS_RU[p.getPeriodMonth() - 1];
            String ruShort = MONTHS_RU_SHORT[p.getPeriodMonth() - 1];
            p.setLabel(ru + " " + p.getPeriodYear());
            p.setShortLabel(ruShort + " " + p.getPeriodYear());
        }
    }
}