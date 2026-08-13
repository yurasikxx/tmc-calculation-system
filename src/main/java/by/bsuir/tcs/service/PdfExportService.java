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
            Paragraph title = new Paragraph("Расчёт потребности в ТМЦ за " + month + "." + year, titleFont);
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
                Paragraph groupTitle = new Paragraph("=== " + typeName + " ===", groupFont);
                document.add(groupTitle);

                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setSpacingBefore(5);
                table.setSpacingAfter(10);

                String[] headers = {"Код ТМЦ", "Наименование", "Ед. изм.", "Количество"};
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.HELVETICA, 10, Font.BOLD)));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

                for (ReportItemDto item : entry.getValue()) {
                    table.addCell(new Phrase(item.getTmcCode()));
                    table.addCell(new Phrase(item.getTmcName()));
                    table.addCell(new Phrase(item.getUnit()));
                    PdfPCell countCell = new PdfPCell(new Phrase(String.valueOf(item.getRequiredQuantity())));
                    countCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(countCell);
                }

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Ошибка при экспорте в PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}