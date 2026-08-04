package by.bsuir.tcs.controller.rest;

import by.bsuir.tcs.dto.ReportItemDto;
import by.bsuir.tcs.service.CalculationService;
import by.bsuir.tcs.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calculations")
@RequiredArgsConstructor
public class CalculationRestController {

    private final CalculationService calculationService;
    private final ReportService reportService;

    @PostMapping("/run")
    public ResponseEntity<List<ReportItemDto>> run(
            @RequestParam Integer year,
            @RequestParam Integer month) {

        calculationService.calculate(year, month);
        List<ReportItemDto> report = reportService.getReportByPeriod(year, month);
        return ResponseEntity.ok(report);
    }
}