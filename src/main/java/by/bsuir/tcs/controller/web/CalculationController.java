package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.ReportItemDto;
import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.service.CalculationResultService;
import by.bsuir.tcs.service.CalculationService;
import by.bsuir.tcs.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/calculations")
@RequiredArgsConstructor
public class CalculationController {

    private final CalculationService calculationService;
    private final ReportService reportService;
    private final CalculationResultService calculationResultService;

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

    @GetMapping("/history")
    public String history(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "calculationDate"));
        Page<CalculationResult> resultPage;

        if (year != null && month != null) {
            resultPage = calculationResultService.findByPeriod(year, month, pageable);
        } else {
            resultPage = calculationResultService.findAll(pageable);
        }

        model.addAttribute("results", resultPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);

        return "calculations/history";
    }

    @GetMapping("/view")
    public String view(
            @RequestParam Integer year,
            @RequestParam Integer month,
            Model model) {

        List<ReportItemDto> report = reportService.getReportByPeriod(year, month);
        Map<String, List<ReportItemDto>> groupedByType = reportService.groupByTmcType(year, month);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("report", report);
        model.addAttribute("groupedByType", groupedByType);

        return "calculations/result";
    }
}