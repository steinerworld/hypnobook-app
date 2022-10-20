package net.steinerworld.hypnobook.ui.views.accounting;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.AccountingType;

public class JournalGrid extends Grid<Accounting> {
   private static final Logger LOGGER = LoggerFactory.getLogger(JournalGrid.class);
   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");


   public interface RemoveListener {
      void remove(Accounting accounting);
   }

   private final List<RemoveListener> removeListenerList = new ArrayList<>();

   public JournalGrid() {
      addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
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
      addColumn(Accounting::getBelegNr)
            .setHeader("Beleg-Nr.")
            .setWidth("140px")
            .setFlexGrow(0);
      addColumn(buchungstextColumnRenderer())
            .setHeader("Buchungstext")
            .setAutoWidth(true)
            .setFlexGrow(1);
      addColumn(new ComponentRenderer<>(Button::new, (button, account) -> {
         button.addThemeVariants(ButtonVariant.LUMO_ICON,
               ButtonVariant.LUMO_ERROR,
               ButtonVariant.LUMO_TERTIARY);
         button.addClickListener(e -> removeAccounting(account));
         button.setIcon(new Icon(VaadinIcon.TRASH));
      }))
            .setWidth("80px")
            .setFlexGrow(0);
      setHeightFull();
   }

   public void addRemoveListener(RemoveListener listener) {
      removeListenerList.add(listener);
   }

   private void removeAccounting(Accounting account) {
      ConfirmDialog dialog = new ConfirmDialog();
      dialog.setHeader("Buchung löschen");
      dialog.setText("Die Buchung " + account.getBelegNr() + " wirklich löschen?");
      dialog.setCancelable(true);
      dialog.setRejectable(false);
      dialog.setConfirmText("Löschen");
      dialog.addConfirmListener(event -> removeListenerList.forEach(rl -> rl.remove(account)));
      dialog.open();
   }

   private static Renderer<Accounting> buchungsdatumColumnRenderer() {
      return LitRenderer.<Accounting>of("<vaadin-vertical-layout>"
                  + "    <span style=\"font-size: var(--lumo-font-size-xs); color: ${item.color};\">"
                  + "      ${item.einzahlung}"
                  + "    </span>"
                  + "    <span style=\"line-height: var(--lumo-line-height-m);\"> "
                  + "      ${item.buchungsdatum}"
                  + "    </span>"
                  + " </vaadin-vertical-layout>")
            .withProperty("color", JournalGrid::einzahlungColorFormatter)
            .withProperty("buchungsdatum", ac -> DATE_FORMAT.format(ac.getBuchungsdatum()))
            .withProperty("einzahlung", JournalGrid::einzahlungTextFormatter);
   }

   private static String einzahlungTextFormatter(Accounting ac) {
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

   private static String einzahlungColorFormatter(Accounting ac) {
      if (ac.getAccountingType() == AccountingType.EINNAHME) {
         if (ac.getEingangsdatum() == null) {
            return "var(--lumo-error-color)";
         } else {
            return "var(--lumo-secondary-text-color)";
         }
      } else {
         return "var(--lumo-secondary-text-color)";
      }
   }

   private static Renderer<Accounting> buchungstextColumnRenderer() {
      return LitRenderer.<Accounting>of("<vaadin-vertical-layout>"
                  + "    <span style=\"font-size: var(--lumo-font-size-xs); color: var(--lumo-secondary-text-color);\">"
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
