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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class JournalSheetService {

   private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
   private static final Font TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
   private static final Font TEXT_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
   private static final Font TEXT_BIGGER_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
   private static final Font TEXT_ITALIC_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.ITALIC, BaseColor.BLACK);
   private static final Logger LOGGER = LoggerFactory.getLogger(JournalSheetService.class);

   public static void main(String[] args) {
      new JournalSheetService().buildPDF();
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

         document.add(table);

         document.add(new Paragraph("Geschäftsinhaberin: Karin Steiner", TEXT_FONT));



         document.close();
         LOGGER.info("done");
      } catch (DocumentException | IOException e) {
         throw new RuntimeException(e);
      }
   }
}
