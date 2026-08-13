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

    public ByteArrayInputStream exportToExcel(Map<String, List<ReportItemDto>> groupedByType, Integer year, Integer month) {
        try (Workbook workbook = new XSSFWorkbook()) {
            String sheetName = "Расчёт ТМЦ за " + month + "-" + year;
            Sheet sheet = workbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Код ТМЦ", "Наименование", "Тип", "Ед. изм.", "Количество"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            int rowNum = 1;
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
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 4));

                for (ReportItemDto item : entry.getValue()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(item.getTmcCode());
                    row.createCell(1).setCellValue(item.getTmcName());
                    row.createCell(2).setCellValue(typeName);
                    row.createCell(3).setCellValue(item.getUnit());
                    row.createCell(4).setCellValue(item.getRequiredQuantity());
                }

                rowNum++;
            }

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
}