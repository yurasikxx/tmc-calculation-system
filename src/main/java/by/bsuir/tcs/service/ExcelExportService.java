package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.ReportItemDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportToExcel(
            Map<String, List<ReportItemDto>> groupedByType, Integer year, Integer month) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Расчёт ТМЦ за " + month + "-" + year);

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Расчёт потребности в ТМЦ за " + month + "." + year);
            titleCell.setCellStyle(createTitleStyle(workbook));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

            Row headerRow = sheet.createRow(2);
            String[] headers = {
                    "Код ТМЦ", "Наименование", "Ед. изм.",
                    "План (норма)", "Приём", "Замена", "Итого"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            int rowNum = 3;

            for (Map.Entry<String, List<ReportItemDto>> entry : groupedByType.entrySet()) {
                String typeName = switch (entry.getKey()) {
                    case "SIZ" -> "СИЗ";
                    case "TOOL" -> "Инструмент";
                    case "EQUIPMENT" -> "Оснастка";
                    default -> entry.getKey();
                };

                Row typeRow = sheet.createRow(rowNum++);
                Cell typeCell = typeRow.createCell(0);
                typeCell.setCellValue("=== " + typeName + " ===");
                typeCell.setCellStyle(createTypeStyle(workbook));
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 6));

                int totalNorm = 0, totalHire = 0, totalRepl = 0, totalAll = 0;

                for (ReportItemDto item : entry.getValue()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(item.getTmcCode());
                    row.createCell(1).setCellValue(item.getTmcName());
                    row.createCell(2).setCellValue(item.getUnit());
                    row.createCell(3).setCellValue(item.getNormBaseQuantity());
                    row.createCell(4).setCellValue(item.getHireQuantity());
                    row.createCell(5).setCellValue(item.getReplacementQuantity());
                    row.createCell(6).setCellValue(item.getTotalQuantity());

                    totalNorm += nz(item.getNormBaseQuantity());
                    totalHire += nz(item.getHireQuantity());
                    totalRepl += nz(item.getReplacementQuantity());
                    totalAll += nz(item.getTotalQuantity());
                }

                Row totalRow = sheet.createRow(rowNum++);
                Cell totalLabel = totalRow.createCell(0);
                totalLabel.setCellValue("Итого по " + typeName);
                totalLabel.setCellStyle(createTotalStyle(workbook));
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));

                totalRow.createCell(3).setCellValue(totalNorm);
                totalRow.createCell(4).setCellValue(totalHire);
                totalRow.createCell(5).setCellValue(totalRepl);
                totalRow.createCell(6).setCellValue(totalAll);

                for (int c = 3; c <= 6; c++) {
                    totalRow.getCell(c).setCellStyle(createTotalStyle(workbook));
                }

                rowNum++;
            }

            Row noteRow = sheet.createRow(rowNum + 1);
            Cell noteCell = noteRow.createCell(0);
            CellStyle noteStyle = workbook.createCellStyle();
            Font noteFont = workbook.createFont();
            noteFont.setItalic(true);
            noteFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
            noteStyle.setFont(noteFont);
            noteCell.setCellStyle(noteStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 0, 6));

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при экспорте в Excel", e);
        }
    }

    public ByteArrayInputStream exportForecastToExcel(
            by.bsuir.tcs.dto.forecast.ForecastResponse forecast,
            by.bsuir.tcs.entity.TmcItem tmc,
            Integer steps) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Прогноз " + tmc.getCode());

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Прогноз потребности: " + tmc.getCode() + " — " + tmc.getName());
            titleCell.setCellStyle(createTitleStyle(workbook));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            Row metaRow = sheet.createRow(2);
            metaRow.createCell(0).setCellValue("Метод:");
            metaRow.createCell(1).setCellValue(forecast.getMethod());
            metaRow.createCell(2).setCellValue("Надёжность:");
            metaRow.createCell(3).setCellValue(reliabilityRussian(forecast.getReliability()));
            metaRow.createCell(4).setCellValue("Горизонт: " + steps + " мес.");

            Row headerRow = sheet.createRow(4);
            String[] headers = {"Месяц", "Прогноз", "Мин (95%)", "Макс (95%)", "Диапазон"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            int rowNum = 5;
            double totalSum = 0, totalMin = 0, totalMax = 0;

            for (by.bsuir.tcs.dto.forecast.ForecastPoint p : forecast.getForecast()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getLabel() != null ? p.getLabel() : ("Месяц " + p.getStep()));
                row.createCell(1).setCellValue(p.getValue());
                row.createCell(2).setCellValue(p.getLowerBound());
                row.createCell(3).setCellValue(p.getUpperBound());
                row.createCell(4).setCellValue(
                        String.format("%.2f — %.2f", p.getLowerBound(), p.getUpperBound()));
                totalSum += p.getValue();
                totalMin += p.getLowerBound();
                totalMax += p.getUpperBound();
            }

            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabel = totalRow.createCell(0);
            totalLabel.setCellValue("Итого за " + steps + " мес.");
            totalLabel.setCellStyle(createTotalStyle(workbook));
            totalRow.createCell(1).setCellValue(totalSum);
            totalRow.createCell(2).setCellValue(totalMin);
            totalRow.createCell(3).setCellValue(totalMax);
            totalRow.createCell(4).setCellValue(
                    String.format("%.2f — %.2f", totalMin, totalMax));
            for (int c = 0; c <= 4; c++) {
                totalRow.getCell(c).setCellStyle(createTotalStyle(workbook));
            }

            Row recRow = sheet.createRow(rowNum + 2);
            Cell recCell = recRow.createCell(0);
            recCell.setCellValue(String.format(
                    "Рекомендация к закупке: %d ед. (в диапазоне от %d до %d)",
                    Math.round(totalSum), Math.round(totalMin), Math.round(totalMax)));
            CellStyle recStyle = workbook.createCellStyle();
            Font recFont = workbook.createFont();
            recFont.setBold(true);
            recFont.setColor(IndexedColors.DARK_GREEN.getIndex());
            recStyle.setFont(recFont);
            recCell.setCellStyle(recStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum + 2, rowNum + 2, 0, 4));

            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при экспорте прогноза в Excel", e);
        }
    }

    private String reliabilityRussian(String reliability) {
        if (reliability == null) return "—";
        return switch (reliability.toLowerCase()) {
            case "high" -> "высокая";
            case "medium" -> "средняя";
            case "low" -> "низкая";
            default -> reliability;
        };
    }

    private int nz(Integer v) {
        return v == null ? 0 : v;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTypeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}