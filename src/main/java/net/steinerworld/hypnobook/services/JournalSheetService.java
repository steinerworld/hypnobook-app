package net.steinerworld.hypnobook.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.AccountingType;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.dto.JournalDto;
import net.steinerworld.hypnobook.exceptions.MaloneyException;
import net.steinerworld.hypnobook.pdf.HeaderFooterPageEvent;

@Service
@RequiredArgsConstructor
public class JournalSheetService {
   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
   private static final DecimalFormat CHF_FORMAT = new DecimalFormat("#,##0.00");

   private final AccountingService accService;

   private static final String EMPTY = "";
   private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
   private static final Font TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
   public static final Font TEXT_SMALL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
   private static final Font TEXT_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
   private static final Font TEXT_ITALIC_SMALLER_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC, BaseColor.BLACK);
   private static final Logger LOGGER = LoggerFactory.getLogger(JournalSheetService.class);

   public JournalDto createDtoSortASC(TaxPeriod tax) {
      return new JournalDto()
            .setCurrentYearCaption(String.valueOf(tax.getGeschaeftsjahr()))
            .setAccountingList(accService.findAllSortedInPeriode(tax, Sort.Direction.ASC));
   }

   public ByteArrayOutputStream streamJournalPDF(JournalDto dto) {
      try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
         Document document = new Document(PageSize.A4, 36, 36, 90, 36);

         PdfWriter writer = PdfWriter.getInstance(document, os);
         writer.setPageEvent(new HeaderFooterPageEvent());

         document.open();

         Paragraph title = new Paragraph("Buchungsjournal " + dto.getCurrentYearCaption(), TITLE_FONT);
         title.setAlignment(Element.ALIGN_CENTER);
         document.add(title);

         Paragraph adr = new Paragraph("Hypnose Steiner, Fabrikstrasse 6, 4556 Biberist", TEXT_FONT);
         adr.setSpacingBefore(10);
         adr.setAlignment(Element.ALIGN_CENTER);
         document.add(adr);

         // Table
         float[] pointColumnWidths = {12f, 13f, 13f, 12f, 50f};
         PdfPTable table = new PdfPTable(pointColumnWidths);
         table.setHeaderRows(1);
         table.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.setWidthPercentage(100);
         table.setSpacingBefore(25);
         table.setSpacingAfter(40);

         // Table title
         table.addCell(buildCellBoldLeft("Datum"));
         table.addCell(buildCellBoldRight("Eingabe"));
         table.addCell(buildCellBoldRightPadding("Ausgabe"));
         table.addCell(buildCellBoldLeft("Beleg-Nr"));
         table.addCell(buildCellBoldLeft("Buchungstext"));

         // Table content
         dto.getAccountingList().forEach(ac -> {
            if (ac.getAccountingType() == AccountingType.EINNAHME) {
               buildIngoingRow(table, ac);
            } else {
               buildOutgoingRow(table, ac);
            }
         });
         document.add(table);

         document.close();
         LOGGER.info("journal pdf created ready to stream");
         return os;
      } catch (DocumentException | IOException e) {
         throw new MaloneyException("cannot create journal pdf sheet", e);
      }
   }

   private void buildOutgoingRow(PdfPTable table, Accounting ac) {
      table.addCell(buildCellLeft(DATE_FORMAT.format(ac.getBuchungsdatum())));
      table.addCell(buildCellRight(EMPTY));
      table.addCell(buildCellRightPadding(CHF_FORMAT.format(ac.getAusgabe())));
      table.addCell(buildCellLeft(ac.getBelegNr()));
      table.addCell(buildBuchungstext(ac.getCategory().getBezeichnung(), ac.getText()));
   }

   private void buildIngoingRow(PdfPTable table, Accounting ac) {
      table.addCell(buildCellLeft(DATE_FORMAT.format(ac.getBuchungsdatum())));
      table.addCell(buildCellRight(CHF_FORMAT.format(ac.getEinnahme())));
      table.addCell(buildCellRight(EMPTY));
      table.addCell(buildCellLeft(ac.getBelegNr()));

      if (ac.getEingangsdatum() == null) {
         String text = "Zahlung ausstehend";
         table.addCell(buildBuchungstext(text, ac.getText()));
      } else {
         String text = "Zahlung erfolgte am " + DATE_FORMAT.format(ac.getEingangsdatum());
         table.addCell(buildBuchungstext(text, ac.getText()));
      }
   }

   private static PdfPCell buildBuchungstext(String rowA, String rowB) {
      PdfPCell cell = buildCell(Element.ALIGN_LEFT, 0);
      cell.addElement(new Phrase(rowA, TEXT_ITALIC_SMALLER_FONT));
      cell.addElement(new Phrase(rowB, TEXT_SMALL_FONT));
      return cell;
   }

   private static PdfPCell buildCellLeft(String text) {
      PdfPCell cell = buildCell(Element.ALIGN_LEFT, 0);
      cell.setPhrase(new Phrase(text, TEXT_FONT));
      return cell;
   }

   private static PdfPCell buildCellRightPadding(String text) {
      PdfPCell cell = buildCell(Element.ALIGN_RIGHT, 10);
      cell.setPhrase(new Phrase(text, TEXT_FONT));
      return cell;
   }

   private static PdfPCell buildCellRight(String text) {
      PdfPCell cell = buildCell(Element.ALIGN_RIGHT, 0);
      cell.setPhrase(new Phrase(text, TEXT_FONT));
      return cell;
   }

   private static PdfPCell buildCellBoldLeft(String text) {
      PdfPCell cell = buildCell(Element.ALIGN_LEFT, 0);
      cell.setPhrase(new Phrase(text, TEXT_BOLD_FONT));
      return cell;
   }

   private static PdfPCell buildCellBoldRightPadding(String text) {
      PdfPCell cell = buildCell(Element.ALIGN_RIGHT, 10);
      cell.setPhrase(new Phrase(text, TEXT_BOLD_FONT));
      return cell;
   }

   private static PdfPCell buildCellBoldRight(String text) {
      PdfPCell cell = buildCell(Element.ALIGN_RIGHT, 0);
      cell.setPhrase(new Phrase(text, TEXT_BOLD_FONT));
      return cell;
   }

   private static PdfPCell buildCell(int align, float rightPadding) {
      PdfPCell cell = new PdfPCell();
      cell.setHorizontalAlignment(align);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setBorderWidthTop(0);
      cell.setBorderWidthRight(0);
      cell.setBorderWidthBottom(1f);
      cell.setBorderWidthLeft(0);
      cell.setPaddingRight(rightPadding);
      cell.setPaddingBottom(5f);
      return cell;
   }

}
