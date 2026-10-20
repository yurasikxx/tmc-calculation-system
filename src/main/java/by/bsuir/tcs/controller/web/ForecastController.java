package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.forecast.ForecastPoint;
import by.bsuir.tcs.dto.forecast.ForecastResponse;
import by.bsuir.tcs.service.ForecastService;
import by.bsuir.tcs.service.TmcItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/forecast")
@RequiredArgsConstructor
public class ForecastController {

    private static final Map<String, String> RELIABILITY_LABELS = Map.of(
            "high", "высокая",
            "medium", "средняя",
            "low", "низкая"
    );

    private static final Map<String, String> RELIABILITY_BADGES = Map.of(
            "high", "bg-success",
            "medium", "bg-warning text-dark",
            "low", "bg-danger"
    );

    private final ForecastService forecastService;
    private final TmcItemService tmcItemService;

    @GetMapping
    public String form(Model model) {
        model.addAttribute("tmcItems", tmcItemService.findAll());
        return "forecast/form";
    }

    @PostMapping("/run")
    public String run(
            @RequestParam Long tmcItemId,
            @RequestParam(defaultValue = "12") Integer steps,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            if (steps < 1 || steps > 36) {
                throw new IllegalArgumentException("Горизонт прогноза должен быть от 1 до 36 месяцев");
            }

            ForecastResponse response = forecastService.forecastForTmc(tmcItemId, steps);

            model.addAttribute("forecast", response.getForecast());
            model.addAttribute("method", humanizeMethod(response.getMethod()));
            model.addAttribute("reliabilityLabel",
                    RELIABILITY_LABELS.getOrDefault(response.getReliability(), "средняя"));
            model.addAttribute("reliabilityBadge",
                    RELIABILITY_BADGES.getOrDefault(response.getReliability(), "bg-secondary"));
            model.addAttribute("tmcName", tmcItemService.findById(tmcItemId).getName());
            model.addAttribute("tmcId", tmcItemId);
            model.addAttribute("steps", steps);

            double totalMean = response.getForecast().stream()
                    .mapToDouble(ForecastPoint::getValue)
                    .sum();
            double totalLower = response.getForecast().stream()
                    .mapToDouble(ForecastPoint::getLowerBound)
                    .sum();
            double totalUpper = response.getForecast().stream()
                    .mapToDouble(ForecastPoint::getUpperBound)
                    .sum();

            model.addAttribute("recommendedQuantity", Math.round(totalMean));
            model.addAttribute("minQuantity", Math.round(totalLower));
            model.addAttribute("maxQuantity", Math.round(totalUpper));
            model.addAttribute("conservativeQuantity", Math.round(totalUpper));

            return "forecast/result";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forecast";
        }
    }

    private String humanizeMethod(String method) {
        if (method == null) return "—";
        return switch (method.toLowerCase()) {
            case "holt_winters" -> "Holt-Winters";
            case "sarima" -> "SARIMA";
            case "moving_average_insufficient_data",
                 "moving_average_fallback" -> "Скользящее среднее";
            default -> method;
        };
    }
}