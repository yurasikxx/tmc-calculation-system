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
            Map<String, List<ReportItemDto>> groupedByType =
                    reportService.groupByTmcType(year, month);

            model.addAttribute("year", year);
            model.addAttribute("month", month);
            model.addAttribute("monthName", monthName(month));
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
            @RequestParam(required = false) String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "periodYear") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Sort.Direction dir = Sort.Direction.fromString(sortDir);
        Sort sort = "tmcItem.name".equals(sortBy)
                ? Sort.by(dir, sortBy).and(Sort.by(dir, "periodYear", "periodMonth"))
                : Sort.by(dir, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CalculationResult> resultPage =
                calculationResultService.findFiltered(year, month, source, pageable);

        model.addAttribute("results", resultPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedSource", source);
        model.addAttribute("years", java.util.stream.IntStream.rangeClosed(2020, 2030).boxed().toList());
        model.addAttribute("sources", java.util.List.of("NORM_BASE", "HIRE", "REPLACEMENT"));
        model.addAttribute("calcController", this);

        return "calculations/history";
    }

    @GetMapping("/view")
    public String view(
            @RequestParam Integer year,
            @RequestParam Integer month,
            Model model) {

        List<ReportItemDto> report = reportService.getReportByPeriod(year, month);
        Map<String, List<ReportItemDto>> groupedByType =
                reportService.groupByTmcType(year, month);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("monthName", monthName(month));
        model.addAttribute("report", report);
        model.addAttribute("groupedByType", groupedByType);

        return "calculations/result";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            calculationResultService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Расчёт успешно удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при удалении расчёта: " + e.getMessage());
        }
        return "redirect:/calculations/history";
    }

    public String monthName(Integer month) {
        if (month == null || month < 1 || month > 12) return "—";
        return MONTHS_RU[month - 1];
    }

    private static final String[] MONTHS_RU = {
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    };
}