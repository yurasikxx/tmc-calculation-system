package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.forecast.ForecastResponse;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.service.ExcelExportService;
import by.bsuir.tcs.service.ForecastService;
import by.bsuir.tcs.service.PdfExportService;
import by.bsuir.tcs.service.TmcItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/forecast/export")
@RequiredArgsConstructor
public class ForecastExportController {

    private final ForecastService forecastService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;
    private final TmcItemService tmcItemService;

    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> exportExcel(
            @RequestParam Long tmcId,
            @RequestParam(defaultValue = "12") Integer steps) {

        TmcItem tmc = tmcItemService.findById(tmcId);
        ForecastResponse forecast = forecastService.forecastForTmc(tmcId, steps);

        ByteArrayInputStream in = excelExportService.exportForecastToExcel(forecast, tmc, steps);
        InputStreamResource resource = new InputStreamResource(in);

        String filename = "Прогноз_ТМЦ_" + tmc.getCode() + "_" + tmcId + ".xlsx";
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encoded)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/pdf")
    public ResponseEntity<InputStreamResource> exportPdf(
            @RequestParam Long tmcId,
            @RequestParam(defaultValue = "12") Integer steps) {

        TmcItem tmc = tmcItemService.findById(tmcId);
        ForecastResponse forecast = forecastService.forecastForTmc(tmcId, steps);

        ByteArrayInputStream in = pdfExportService.exportForecastToPdf(forecast, tmc, steps);
        InputStreamResource resource = new InputStreamResource(in);

        String filename = "Прогноз_ТМЦ_" + tmc.getCode() + "_" + tmcId + ".pdf";
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encoded)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}