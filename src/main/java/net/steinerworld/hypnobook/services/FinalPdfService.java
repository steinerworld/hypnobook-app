package net.steinerworld.hypnobook.services;

import java.io.FileOutputStream;
import java.io.IOException;

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

public class FinalPdfService {
   private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
   private static final Font TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
   private static final Font TEXT_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
   private static final Font TEXT_BIGGER_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
   private static final Font TEXT_ITALIC_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.ITALIC, BaseColor.BLACK);
   private static final Logger LOGGER = LoggerFactory.getLogger(FinalPdfService.class);

   public static void main(String[] args) {
      new FinalPdfService().buildPDF();
   }

   public void buildPDF() {

      try (FileOutputStream os = new FileOutputStream("test.pdf")) {
         Document document = new Document();
         PdfWriter.getInstance(document, os);
         document.open();

         document.add(new Paragraph("Jahresabschluss 2022", TITLE_FONT));

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
         table.addCell(buildCellRight("2021"));
         table.addCell(buildCellBoldRight("2022"));

         // Einkommen
         table.addCell(buildCellTitle("Einkommen aus Geschäftsbetrieb"));
         table.addCell(buildCellBoldLeftPadding("Total Einkommen"));
         table.addCell(buildCellRight("3'512.00"));
         table.addCell(buildCellBoldRight("4'794.00"));

         // Geschäftsaufwand
         table.addCell(buildCellTitle("Geschäftsaufwand"));
         table.addCell(buildCellLeftPadding("Miete Praxis"));
         table.addCell(buildCellRight("811.00"));
         table.addCell(buildCellRight("853.00"));
         table.addCell(buildCellLeftPadding("Werbung/Marketing"));
         table.addCell(buildCellRight("456.00"));
         table.addCell(buildCellRight("514.00"));
         table.addCell(buildCellBoldLeftPadding("Total Geschäftsaufwand"));
         table.addCell(buildCellRight("1'267.00"));
         table.addCell(buildCellBoldRight("1'367.00"));

         // Total
         table.addCell(buildCellTitle("Einkommen aus selbstständiger Erwerbstätigkeit"));
         table.addCell(buildCellBoldLeftPadding("Gewinn"));
         table.addCell(buildCellRight("2'245.00"));
         table.addCell(buildCellBoldRight("3'427.00"));

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
