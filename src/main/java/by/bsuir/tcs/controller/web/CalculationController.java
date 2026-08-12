package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.ReportItemDto;
import by.bsuir.tcs.service.CalculationService;
import by.bsuir.tcs.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/calculations")
@RequiredArgsConstructor
public class CalculationController {

    private final CalculationService calculationService;
    private final ReportService reportService;

    @GetMapping
    public String form() {
        return "calculations/form";
    }

    @PostMapping("/run")
    public String run(
            @RequestParam Integer year,
            @RequestParam Integer month,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            calculationService.calculate(year, month);

            List<ReportItemDto> report = reportService.getReportByPeriod(year, month);
            Map<String, List<ReportItemDto>> groupedByType = reportService.groupByTmcType(year, month);

            model.addAttribute("year", year);
            model.addAttribute("month", month);
            model.addAttribute("report", report);
            model.addAttribute("groupedByType", groupedByType);

            return "calculations/result";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при расчёте: " + e.getMessage());
            return "redirect:/calculations";
        }
    }
}