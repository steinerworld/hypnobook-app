package net.steinerworld.hypnobook.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import net.steinerworld.hypnobook.services.JournalSheetService;

/*
https://memorynotfound.com/adding-header-footer-pdf-using-itext-java/
https://www.javamadesoeasy.com/2016/06/how-to-set-header-and-footer-in-pdf-in.html
 */
public class HeaderFooterPageEvent extends PdfPageEventHelper {

   public void onStartPage(PdfWriter writer, Document document) {
      Rectangle rect = writer.getPageSize();

      PdfPTable header = new PdfPTable(2);
      try {
         // set defaults
         header.setWidths(new int[] {2, 24});
         header.setTotalWidth(rect.getWidth());
         header.getDefaultCell().setFixedHeight(40);
         header.getDefaultCell().setBorder(Rectangle.BOTTOM);
         header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

         // add image
         PdfPCell logo = new PdfPCell();
         logo.setPaddingBottom(15);
         logo.setPaddingLeft(10);
         logo.setBorder(Rectangle.BOTTOM);
         logo.setBorderColor(BaseColor.LIGHT_GRAY);
         logo.addElement(new Phrase("LOGO :-)", new Font(Font.FontFamily.HELVETICA, 12)));
         header.addCell(logo);

         //         Image logo = Image.getInstance(HeaderFooterPageEvent.class.getResource("/icons/icon.png"));
         //         header.addCell(logo);


         // add text
         PdfPCell text = new PdfPCell();
         text.setPaddingBottom(15);
         text.setPaddingLeft(10);
         text.setBorder(Rectangle.BOTTOM);
         text.setBorderColor(BaseColor.LIGHT_GRAY);
         text.addElement(new Phrase("Buchungsjournal", new Font(Font.FontFamily.HELVETICA, 12)));
         text.addElement(new Phrase("Hypnose Steiner, Fabrikstrasse 6, 4556 Biberist", new Font(Font.FontFamily.HELVETICA, 8)));
         header.addCell(text);

         // write content
         header.writeSelectedRows(0, -1, rect.getLeft(), rect.getTop() - 10, writer.getDirectContent());
      } catch (DocumentException e) {
         throw new ExceptionConverter(e);
      }
   }

   @Override
   public void onEndPage(PdfWriter pdfWriter, Document document) {
      Rectangle rect = pdfWriter.getPageSize();

      Phrase pageText = new Phrase(String.format("Seite %d", pdfWriter.getPageNumber()), JournalSheetService.TEXT_SMALL_FONT);
      ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER, pageText, rect.getRight() / 2, rect.getBottom() + 20, 0);
   }


}
