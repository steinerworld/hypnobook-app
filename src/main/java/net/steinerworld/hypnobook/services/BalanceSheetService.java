package net.steinerworld.hypnobook.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.dto.BalanceDto;
import net.steinerworld.hypnobook.exceptions.MaloneyException;

@Service
@RequiredArgsConstructor
public class BalanceSheetService {
   private static final Logger LOGGER = LoggerFactory.getLogger(BalanceSheetService.class);
   private static final DecimalFormat DF = new DecimalFormat("#,##0.00");
   private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
   private static final Font TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
   private static final Font TEXT_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
   private static final Font TEXT_BIGGER_BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
   private static final Font TEXT_ITALIC_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.ITALIC, BaseColor.BLACK);
   private static final String EMPTY = "";

   private final AccountingService accountingService;

   public BalanceDto createDto(TaxPeriod tp, Optional<TaxPeriod> maybeLast) {
      int currJahr = tp.getGeschaeftsjahr();
      double currIngoing = accountingService.sumEinnahmenInPeriode(tp);
      double currOutgoing = accountingService.sumAusgabenInPeriode(tp);
      double currResult = currIngoing - currOutgoing;
      String resultCaption = currResult < 0 ? "Verlust" : "Gewinn";

      BalanceDto dto = new BalanceDto()
            .setCurrentYearCaption(String.valueOf(currJahr))
            .setSumTotalIngoingCurrentYear(DF.format(currIngoing))
            .setSumTotalOutgoingCurrentYear(DF.format(currOutgoing))
            .setResultCaption(resultCaption)
            .setResultCurrentYear(DF.format(currResult));
      LOGGER.info("current tax-period is set");

      maybeLast.ifPresent(ltp -> {
         int lastJahr = ltp.getGeschaeftsjahr();
         double lastIngoing = accountingService.sumEinnahmenInPeriode(ltp);
         double lastOutgoing = accountingService.sumAusgabenInPeriode(ltp);
         double lastResult = lastIngoing - lastOutgoing;
         dto.setLastYearCaption(String.valueOf(lastJahr))
               .setSumTotalIngoingLastYear(DF.format(lastIngoing))
               .setSumTotalOutgoingLastYear(DF.format(lastOutgoing))
               .setResultLastYear(DF.format(lastResult));
         LOGGER.info("last tax-period is set");
      });
      LOGGER.info("created dto is {}", dto);
      return dto;
   }

   public BalanceDto.SumByCategory createCatSummary(Category cat, TaxPeriod tp, Optional<TaxPeriod> maybeLast) {
      BalanceDto.SumByCategory item;
      double sumCat = accountingService.sumAusgabeInTaxPeriodAndCategory(tp, cat);
      if (maybeLast.isPresent()) {
         double sumLast = accountingService.sumAusgabeInTaxPeriodAndCategory(maybeLast.get(), cat);
         item = new BalanceDto.SumByCategory(cat.getBezeichnung(), DF.format(sumCat), DF.format(sumLast));
      } else {
         item = new BalanceDto.SumByCategory(cat.getBezeichnung(), DF.format(sumCat), EMPTY);
      }
      LOGGER.info("category: {}", item);
      return item;
   }

   public ByteArrayOutputStream streamBalancePdfSheet(BalanceDto dto) {
      try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
         Document document = new Document();
         PdfWriter.getInstance(document, os);
         document.open();

         document.add(new Paragraph("Geschäftsjahr " + dto.getCurrentYearCaption(), TITLE_FONT));

         Paragraph adr = new Paragraph();
         adr.setSpacingBefore(10);
         adr.setFont(TEXT_FONT);
         adr.add("Hypnose Steiner\nFabrikstrasse 6\n4562 Biberist");
         document.add(adr);

         float[] pointColumnWidths = {3f, 1f, 1f};
         PdfPTable table = new PdfPTable(pointColumnWidths);
         table.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.setWidthPercentage(100);
         table.setSpacingBefore(25);
         table.setSpacingAfter(40);

         // Titel
         table.addCell(buildCellBoldLeft(EMPTY));
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

         // Unterschrift Felder
         document.add(new Paragraph("Geschäftsinhaberin: Karin Steiner", TEXT_FONT));

         PdfPTable footerTable = new PdfPTable(2);
         footerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
         footerTable.setWidthPercentage(50);
         footerTable.setSpacingBefore(55);
         footerTable.addCell(buildCellItalicBorderTop("Ort und Datum"));
         footerTable.addCell(buildCellItalicBorderTop("Unterschrift"));
         document.add(footerTable);

         document.close();
         LOGGER.info("pdf create done stream it");
         return os;
      } catch (DocumentException | IOException e) {
         throw new MaloneyException("cannot create balance pdf sheet", e);
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
