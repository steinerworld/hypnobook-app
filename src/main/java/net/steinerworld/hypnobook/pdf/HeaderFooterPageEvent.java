package net.steinerworld.hypnobook.pdf;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import net.steinerworld.hypnobook.exceptions.MaloneyException;
import net.steinerworld.hypnobook.services.JournalSheetService;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

   public void onStartPage(PdfWriter writer, Document document) {
      Rectangle rect = writer.getPageSize();

      PdfPTable header = new PdfPTable(2);
      try {
         // set defaults
         header.setWidths(new int[] {1, 3});
         header.setTotalWidth(rect.getWidth());
         header.getDefaultCell().setFixedHeight(40);
         header.getDefaultCell().setBorder(Rectangle.BOTTOM);
         header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

         // add image
         Image logo = Image.getInstance(HeaderFooterPageEvent.class.getResource("/serversideimages/LogoHypno.jpg"));
         PdfPCell kopf = new PdfPCell();
         kopf.setPaddingBottom(15);
         kopf.setPaddingLeft(25);
         kopf.setBorder(Rectangle.BOTTOM);
         kopf.setBorderColor(BaseColor.LIGHT_GRAY);
         kopf.addElement(logo);
         header.addCell(kopf);

         // add text
         PdfPCell text = new PdfPCell();
         text.setPaddingTop(10);
         text.setPaddingBottom(15);
         text.setPaddingRight(25);

         text.setBorder(Rectangle.BOTTOM);
         text.setBorderColor(BaseColor.LIGHT_GRAY);
         Paragraph p1 = new Paragraph("Hypnose Steiner", new Font(Font.FontFamily.HELVETICA, 10));
         p1.setAlignment(Element.ALIGN_RIGHT);
         text.addElement(p1);

         Paragraph p2 = new Paragraph("Fabrikstrasse 6, 4556 Biberist", new Font(Font.FontFamily.HELVETICA, 8));
         p2.setAlignment(Element.ALIGN_RIGHT);
         text.addElement(p2);
         header.addCell(text);

         // write content
         header.writeSelectedRows(0, -1, rect.getLeft(), rect.getTop() - 10, writer.getDirectContent());
      } catch (DocumentException | IOException e) {
         throw new MaloneyException("cannot build Header or Footer section", e);
      }
   }

   @Override
   public void onEndPage(PdfWriter pdfWriter, Document document) {
      Rectangle rect = pdfWriter.getPageSize();

      Phrase pageText = new Phrase(String.format("Seite %d", pdfWriter.getPageNumber()), JournalSheetService.TEXT_SMALL_FONT);
      ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER, pageText, rect.getRight() / 2, rect.getBottom() + 20, 0);
   }


}
