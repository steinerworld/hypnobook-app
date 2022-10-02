package net.steinerworld.hypnobook.ui.views.accounting;

import com.vaadin.flow.component.grid.Grid;

import net.steinerworld.hypnobook.domain.Accounting;

public class Journal extends Grid<Accounting> {

   public Journal() {
      addColumn(Accounting::getBuchungsdatum).setHeader("Datum").setAutoWidth(true);
      addColumn(Accounting::getEinnahme).setHeader("Einnahme").setAutoWidth(true);
      addColumn(Accounting::getAusgabe).setHeader("Ausgabe").setAutoWidth(true);
      addColumn(Accounting::getBelegNr).setHeader("Beleg-Nr.").setAutoWidth(true);
      addColumn(Accounting::getText).setHeader("Buchungstext").setAutoWidth(true);
      setHeightFull();
   }

}
