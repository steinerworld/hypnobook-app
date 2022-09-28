package net.steinerworld.hypnobook.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import net.steinerworld.hypnobook.dto.BalanceDto;

public class Balance {
   private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
   private static final Font TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
   private static final Font TEXT_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
   private static final Font TEXT_BIGGER_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
   private static final Font TEXT_ITALIC_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.ITALIC, BaseColor.BLACK);
   private static final Logger LOGGER = LoggerFactory.getLogger(Balance.class);

   public static void main(String[] args) {
      BalanceDto.SumByCategory c1 = new BalanceDto.SumByCategory("Miete Praxis", "853.00", "811.00");
      BalanceDto.SumByCategory c2 = new BalanceDto.SumByCategory("Werbung/Marketing", "514.00", "456.00");
      List<BalanceDto.SumByCategory> list = Arrays.asList(c1, c2);
      BalanceDto dto = new BalanceDto()
            .setCurrentYearCaption("2022")
            .setLastYearCaption("2021")
            .setSumTotalIngoingCurrentYear("4'794.00")
            .setSumTotalIngoingLastYear("3'512.00")
            .setSumTotalOutgoingCurrentYear("1'367.00")
            .setSumTotalOutgoingLastYear("1'267.00")
            .setResultCaption("Gewinn")
            .setResultCurrentYear("3'427.00")
            .setResultLastYear("2'245.00")
            .setCategoryList(list);

      new Balance().buildPDF(dto);
   }

   public void buildPDF(BalanceDto dto) {

      try (FileOutputStream os = new FileOutputStream("test.pdf")) {
         Document document = new Document();
         PdfWriter.getInstance(document, os);
         document.open();

         document.add(new Paragraph("Geschäftsjahr" + dto.getCurrentYearCaption(), TITLE_FONT));

         Paragraph adr = new Paragraph();
         adr.setSpacingBefore(10);
         adr.setFont(TEXT_FONT);
         adr.add("Hypnose Steiner\nFabrikstrasse 6\n4556 Biberist");
         document.add(adr);

         float[] pointColumnWidths = {3f, 1f, 1f};
         PdfPTable table = new PdfPTable(pointColumnWidths);
         table.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.setWidthPercentage(100);
         table.setSpacingBefore(25);
         table.setSpacingAfter(40);

         // Titel
         table.addCell(buildCellBoldLeft(""));
         table.addCell(buildCellRight(dto.getLastYearCaption()));
         table.addCell(buildCellBoldRight(dto.getCurrentYearCaption()));

         // Einkommen
         table.addCell(buildCellTitle("Einkommen aus Geschäftsbetrieb"));
         table.addCell(buildCellBoldLeftPadding("Total Einkommen"));
         table.addCell(buildCellRight(dto.getSumTotalIngoingLastYear()));
         table.addCell(buildCellBoldRight(dto.getSumTotalIngoingCurrentYear()));

         // Geschäftsaufwand
         table.addCell(buildCellTitle("Geschäftsaufwand"));
         for (BalanceDto.SumByCategory cat : dto.getCategoryList()) {
            table.addCell(buildCellLeftPadding(cat.getCategory()));
            table.addCell(buildCellRight(cat.getSumByCategoryLastYear()));
            table.addCell(buildCellRight(cat.getSumByCategoryCurrentYear()));
         }
         table.addCell(buildCellBoldLeftPadding("Total Geschäftsaufwand"));
         table.addCell(buildCellRight(dto.getSumTotalOutgoingLastYear()));
         table.addCell(buildCellBoldRight(dto.getSumTotalOutgoingCurrentYear()));

         // Total
         table.addCell(buildCellTitle("Einkommen aus selbstständiger Erwerbstätigkeit"));
         table.addCell(buildCellBoldLeftPadding(dto.getResultCaption()));
         table.addCell(buildCellRight(dto.getResultLastYear()));
         table.addCell(buildCellBoldRight(dto.getResultCurrentYear()));

         document.add(table);

         document.add(new Paragraph("Geschäftsinhaberin: Karin Steiner", TEXT_FONT));

         PdfPTable footerTable = new PdfPTable(2);
         footerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
         footerTable.setWidthPercentage(50);
         footerTable.setSpacingBefore(55);
         footerTable.addCell(buildCellItalicBorderTop("Ort und Datum"));
         footerTable.addCell(buildCellItalicBorderTop("Unterschrift"));
         document.add(footerTable);



         document.close();
         LOGGER.info("done");
      } catch (DocumentException | IOException e) {
         throw new RuntimeException(e);
      }
   }

   private static PdfPCell buildCellLeft(String text) {
      return buildCell(Element.ALIGN_LEFT, 1, text, TEXT_FONT, 0);
   }

   private static PdfPCell buildCellLeftPadding(String text) {
      return buildCell(Element.ALIGN_LEFT, 1, text, TEXT_FONT, 10);
   }

   private static PdfPCell buildCellRight(String text) {
      return buildCell(Element.ALIGN_RIGHT, 1, text, TEXT_FONT, 0);
   }

   private static PdfPCell buildCellTitle(String text) {
      return buildCell(Element.ALIGN_LEFT, 3, text, TEXT_BIGGER_BOLD_FONT, 0);
   }

   private static PdfPCell buildCellBoldLeft(String text) {
      return buildCell(Element.ALIGN_LEFT, 1, text, TEXT_BOLD_FONT, 0);
   }

   private static PdfPCell buildCellBoldLeftPadding(String text) {
      return buildCell(Element.ALIGN_LEFT, 1, text, TEXT_BOLD_FONT, 10);
   }

   private static PdfPCell buildCellBoldRight(String text) {
      return buildCell(Element.ALIGN_RIGHT, 1, text, TEXT_BOLD_FONT, 0);
   }

   private static PdfPCell buildCellItalicBorderTop(String text) {
      PdfPCell cell = buildCell(Element.ALIGN_LEFT, 1, text, TEXT_ITALIC_FONT, 0);
      cell.setBorderWidthTop(1f);
      cell.setBorderWidthBottom(0);
      return cell;
   }

   private static PdfPCell buildCell(int align, int colspan, String text, Font font, float leftPadding) {
      PdfPCell cell = new PdfPCell();
      cell.setPhrase(new Phrase(text, font));
      cell.setHorizontalAlignment(align);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setColspan(colspan);
      cell.setBorderWidthTop(0);
      cell.setBorderWidthRight(0);
      cell.setBorderWidthBottom(1f);
      cell.setBorderWidthLeft(0);
      cell.setPaddingLeft(leftPadding);
      cell.setPaddingTop(5f);
      cell.setPaddingBottom(5f);
      return cell;
   }

}
