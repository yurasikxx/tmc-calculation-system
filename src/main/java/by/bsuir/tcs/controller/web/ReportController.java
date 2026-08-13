package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.ReportItemDto;
import by.bsuir.tcs.service.ExcelExportService;
import by.bsuir.tcs.service.PdfExportService;
import by.bsuir.tcs.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel(
            @RequestParam Integer year,
            @RequestParam Integer month) {

        Map<String, List<ReportItemDto>> groupedByType = reportService.groupByTmcType(year, month);

        InputStreamResource resource = new InputStreamResource(
                excelExportService.exportToExcel(groupedByType, year, month)
        );

        String filename = "Расчет_ТМЦ_" + month + "_" + year + ".xlsx";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedFilename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportPdf(
            @RequestParam Integer year,
            @RequestParam Integer month) {

        Map<String, List<ReportItemDto>> groupedByType = reportService.groupByTmcType(year, month);

        InputStreamResource resource = new InputStreamResource(
                pdfExportService.exportToPdf(groupedByType, year, month)
        );

        String filename = "Расчет_ТМЦ_" + month + "_" + year + ".pdf";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedFilename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}