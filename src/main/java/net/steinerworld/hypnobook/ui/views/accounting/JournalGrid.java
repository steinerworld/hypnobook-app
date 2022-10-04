package net.steinerworld.hypnobook.ui.views.accounting;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.AccountingType;

public class JournalGrid extends Grid<Accounting> {
   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");


   public JournalGrid() {

      setSelectionMode(SelectionMode.SINGLE);
      setAllRowsVisible(true);
      addColumn(buchungsdatumColumnRenderer())
            .setHeader("Datum")
            .setWidth("200px")
            .setFlexGrow(0);
      addColumn(new NumberRenderer<>(Accounting::getEinnahme, "%(,.2f", Locale.getDefault(), ""))
            .setHeader("Einnahme")
            .setWidth("150px")
            .setFlexGrow(0);
      addColumn(new NumberRenderer<>(Accounting::getAusgabe, "%(,.2f", Locale.getDefault(), ""))
            .setHeader("Ausgabe")
            .setWidth("150px")
            .setFlexGrow(0);
      addColumn(Accounting::getBelegNr).setHeader("Beleg-Nr.")
            .setAutoWidth(true)
            .setFlexGrow(0);
      addColumn(buchungstextColumnRenderer()).setHeader("Buchungstext")
            .setAutoWidth(true)
            .setFlexGrow(1);
      setHeightFull();
   }

   private static Renderer<Accounting> buchungsdatumColumnRenderer() {
      return LitRenderer.<Accounting>of("<vaadin-vertical-layout>"
                  + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                  + "      ${item.einzahlung}"
                  + "    </span>"
                  + "    <span style=\"line-height: var(--lumo-line-height-m);\"> "
                  + "      ${item.buchungsdatum}"
                  + "    </span>"
                  + " </vaadin-vertical-layout>")
            .withProperty("buchungsdatum", ac -> DATE_FORMAT.format(ac.getBuchungsdatum()))
            .withProperty("einzahlung", JournalGrid::einzahlungFormatter);
   }

   private static String einzahlungFormatter(Accounting ac) {
      if (ac.getAccountingType() == AccountingType.EINNAHME) {
         if (ac.getEingangsdatum() == null) {
            return "Zahlung ausstehend";
         } else {
            return "Zahlung am " + DATE_FORMAT.format(ac.getEingangsdatum());
         }
      } else {
         return "";
      }
   }

   private static Renderer<Accounting> buchungstextColumnRenderer() {
      return LitRenderer.<Accounting>of("<vaadin-vertical-layout>"
                  + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                  + "      ${item.category}"
                  + "    </span>"
                  + "    <span style=\"line-height: var(--lumo-line-height-m);\"> "
                  + "      ${item.buchungstext}"
                  + "    </span>"
                  + " </vaadin-vertical-layout>")
            .withProperty("category", JournalGrid::categoryText)
            .withProperty("buchungstext", Accounting::getText);
   }

   private static String categoryText(Accounting ac) {
      if (ac.getAccountingType() == AccountingType.AUSGABE) {
         return ac.getCategory().getBezeichnung();
      } else {
         return "";
      }
   }

}
