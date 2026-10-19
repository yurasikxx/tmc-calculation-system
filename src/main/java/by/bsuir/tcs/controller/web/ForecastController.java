package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.forecast.ForecastResponse;
import by.bsuir.tcs.service.ForecastService;
import by.bsuir.tcs.service.TmcItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/forecast")
@RequiredArgsConstructor
public class ForecastController {

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
            @RequestParam(defaultValue = "3") Integer steps,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            ForecastResponse response = forecastService.forecastForTmc(tmcItemId, steps);
            model.addAttribute("forecast", response.getForecast());
            model.addAttribute("method", response.getMethod());
            model.addAttribute("tmcName", tmcItemService.findById(tmcItemId).getName());
            model.addAttribute("steps", steps);
            return "forecast/result";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forecast";
        }
    }
}