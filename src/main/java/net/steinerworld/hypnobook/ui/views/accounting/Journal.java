package net.steinerworld.hypnobook.ui.views.accounting;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.AccountingType;

public class Journal extends Grid<Accounting> {
   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");


   public Journal() {

      setSelectionMode(SelectionMode.SINGLE);
      setAllRowsVisible(true);
      addColumn(createBuchungsdatumRenderer())
            .setHeader("Datum")
            .setAutoWidth(true)
            .setFlexGrow(0);

      //      addColumn(new LocalDateRenderer<>(Accounting::getBuchungsdatum, "dd.MM.yyyy"))
      //            .setHeader("Datum")
      //            .setAutoWidth(true)
      //            .setFlexGrow(0);

      addColumn(new NumberRenderer<>(Accounting::getEinnahme, "%(,.2f", Locale.getDefault(), ""))
            .setHeader("Einnahme")
            .setAutoWidth(true)
            .setFlexGrow(0);
      addColumn(new NumberRenderer<>(Accounting::getAusgabe, "%(,.2f", Locale.getDefault(), ""))
            .setHeader("Ausgabe")
            .setAutoWidth(true)
            .setFlexGrow(0);
      addColumn(Accounting::getBelegNr).setHeader("Beleg-Nr.")
            .setAutoWidth(true)
            .setFlexGrow(0);
      addColumn(Accounting::getText).setHeader("Buchungstext")
            .setAutoWidth(true)
            .setFlexGrow(1);
      setHeightFull();
   }

   private static Renderer<Accounting> createBuchungsdatumRenderer() {
      return LitRenderer.<Accounting>of("<vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                  + "    <span> ${item.buchungsdatum} </span>"
                  + "    <span style=\\\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                  + "        ${item.einzahlung}"
                  + "    </span>"
                  + " </vaadin-vertical-layout>")
            .withProperty("buchungsdatum", ac -> DATE_FORMAT.format(ac.getBuchungsdatum()))
            .withProperty("einzahlung", Journal::einzahlungFormatter);
   }

   private static String einzahlungFormatter(Accounting ac) {
      if (ac.getAccountingType() == AccountingType.EINNAHME && ac.getEingangsdatum() == null) {
         return "Ausstehend";
      } else {
         return " ";
      }
   }

}
