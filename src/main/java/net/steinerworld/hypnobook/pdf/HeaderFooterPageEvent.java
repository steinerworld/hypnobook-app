package net.steinerworld.hypnobook.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import net.steinerworld.hypnobook.services.JournalSheetService;

/*
https://memorynotfound.com/adding-header-footer-pdf-using-itext-java/
https://www.javamadesoeasy.com/2016/06/how-to-set-header-and-footer-in-pdf-in.html
 */
public class HeaderFooterPageEvent extends PdfPageEventHelper {

   public void onStartPage(PdfWriter pdfWriter, Document document) {
      System.out.println("onStartPage() method > Writing header in file");
      //      Rectangle rect = pdfWriter.getBoxSize("rectangle");
      Rectangle rect = pdfWriter.getPageSize();


      // TOP LEFT
      ColumnText.showTextAligned(pdfWriter.getDirectContent(),
            Element.ALIGN_CENTER, new Phrase("TOP LEFT"), rect.getLeft(),
            rect.getTop() - 30, 0);

      // TOP MEDIUM
      ColumnText.showTextAligned(pdfWriter.getDirectContent(),
            Element.ALIGN_CENTER, new Phrase("TOP MEDIUM"),
            rect.getRight() / 2, rect.getTop() - 30, 0);

      // TOP RIGHT
      ColumnText.showTextAligned(pdfWriter.getDirectContent(),
            Element.ALIGN_CENTER, new Phrase("TOP RIGHT"), rect.getRight() - 30,
            rect.getTop() - 30, 0);
   }

   @Override
   public void onEndPage(PdfWriter pdfWriter, Document document) {
      Rectangle rect = pdfWriter.getPageSize();

      Phrase pageText = new Phrase(String.format("Seite %d", pdfWriter.getPageNumber()), JournalSheetService.TEXT_SMALL_FONT);
      ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER, pageText, rect.getRight() / 2, rect.getBottom() + 20, 0);
   }


}
