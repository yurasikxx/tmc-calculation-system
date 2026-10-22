package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.ReportItemDto;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Service
public class PdfExportService {

    public ByteArrayInputStream exportToPdf(Map<String, List<ReportItemDto>> groupedByType, Integer year, Integer month) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph(
                    "Расчёт потребности в ТМЦ за " + month + "." + year, titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            for (Map.Entry<String, List<ReportItemDto>> entry : groupedByType.entrySet()) {
                String typeName = switch (entry.getKey()) {
                    case "SIZ" -> "СИЗ";
                    case "TOOL" -> "Инструмент";
                    case "EQUIPMENT" -> "Оснастка";
                    default -> entry.getKey();
                };

                Font groupFont = new Font(Font.HELVETICA, 12, Font.BOLD);
                document.add(new Paragraph("=== " + typeName + " ===", groupFont));

                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1.5f, 4f, 1f, 1.5f, 1.5f, 1.5f, 1.5f});
                table.setSpacingBefore(5);
                table.setSpacingAfter(10);

                String[] headers = {
                        "Код", "Наименование", "Ед.",
                        "План", "Приём", "Замена*", "Итого"
                };
                Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
                for (String h : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

                Font cellFont = new Font(Font.HELVETICA, 10);
                int totalNorm = 0, totalHire = 0, totalRepl = 0, totalAll = 0;

                for (ReportItemDto item : entry.getValue()) {
                    table.addCell(new Phrase(item.getTmcCode(), cellFont));
                    table.addCell(new Phrase(item.getTmcName(), cellFont));
                    table.addCell(new Phrase(item.getUnit(), cellFont));
                    table.addCell(rightAligned(item.getNormBaseQuantity(), cellFont));
                    table.addCell(rightAligned(item.getHireQuantity(), cellFont));
                    table.addCell(rightAligned(item.getReplacementQuantity(), cellFont));
                    table.addCell(rightAligned(item.getTotalQuantity(), cellFont));

                    totalNorm += nz(item.getNormBaseQuantity());
                    totalHire += nz(item.getHireQuantity());
                    totalRepl += nz(item.getReplacementQuantity());
                    totalAll += nz(item.getTotalQuantity());
                }

                Font totalFont = new Font(Font.HELVETICA, 10, Font.BOLD);
                PdfPCell totalLabel = new PdfPCell(new Phrase("Итого по " + typeName, totalFont));
                totalLabel.setColspan(3);
                totalLabel.setBackgroundColor(new Color(255, 250, 205));
                table.addCell(totalLabel);
                table.addCell(rightAligned(totalNorm, totalFont));
                table.addCell(rightAligned(totalHire, totalFont));
                table.addCell(rightAligned(totalRepl, totalFont));
                table.addCell(rightAligned(totalAll, totalFont));

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            Font noteFont = new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY);
            document.add(new Paragraph(
                    "* Замена — часть Плана (средний износ), показана для информации.",
                    noteFont));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Ошибка при экспорте в PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportForecastToPdf(
            by.bsuir.tcs.dto.forecast.ForecastResponse forecast,
            by.bsuir.tcs.entity.TmcItem tmc,
            Integer steps) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document doc = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Прогноз потребности в ТМЦ", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(title);

            Font subFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Paragraph subtitle = new Paragraph(
                    tmc.getCode() + " — " + tmc.getName(), subFont);
            subtitle.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(subtitle);
            doc.add(Chunk.NEWLINE);

            Font metaFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            doc.add(new Paragraph("Метод: " + forecast.getMethod(), metaFont));
            doc.add(new Paragraph("Надёжность: " + reliabilityRussian(forecast.getReliability()), metaFont));
            doc.add(new Paragraph("Горизонт прогноза: " + steps + " мес.", metaFont));
            doc.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 2f, 2f, 2f, 3f});
            table.setSpacingAfter(10);

            String[] headers = {"Месяц", "Прогноз", "Мин (95%)", "Макс (95%)", "Диапазон"};
            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(Color.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            Font cellFont = new Font(Font.HELVETICA, 10);
            double totalSum = 0, totalMin = 0, totalMax = 0;

            for (by.bsuir.tcs.dto.forecast.ForecastPoint p : forecast.getForecast()) {
                table.addCell(new Phrase(p.getLabel() != null ? p.getLabel() : ("Месяц " + p.getStep()), cellFont));
                table.addCell(rightAligned(p.getValue(), cellFont));
                table.addCell(rightAligned(p.getLowerBound(), cellFont));
                table.addCell(rightAligned(p.getUpperBound(), cellFont));
                table.addCell(new Phrase(String.format("%.2f — %.2f",
                        p.getLowerBound(), p.getUpperBound()), cellFont));

                totalSum += p.getValue();
                totalMin += p.getLowerBound();
                totalMax += p.getUpperBound();
            }

            Font totalFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            PdfPCell totalLabel = new PdfPCell(new Phrase("Итого за " + steps + " мес.", totalFont));
            totalLabel.setBackgroundColor(new Color(255, 250, 205));
            table.addCell(totalLabel);
            table.addCell(rightAligned(totalSum, totalFont));
            table.addCell(rightAligned(totalMin, totalFont));
            table.addCell(rightAligned(totalMax, totalFont));
            table.addCell(new Phrase(String.format("%.2f — %.2f", totalMin, totalMax), totalFont));

            doc.add(table);

            Font recFont = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(0, 100, 0));
            doc.add(new Paragraph(String.format(
                    "Рекомендация к закупке: %d ед. (в диапазоне от %d до %d)",
                    Math.round(totalSum), Math.round(totalMin), Math.round(totalMax)), recFont));

            doc.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Ошибка при экспорте прогноза в PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
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

    private PdfPCell rightAligned(double value, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(String.format("%.2f", value), font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private int nz(Integer v) {
        return v == null ? 0 : v;
    }

    private PdfPCell rightAligned(Integer value, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(nz(value)), font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell rightAligned(int value, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(value), font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }
}