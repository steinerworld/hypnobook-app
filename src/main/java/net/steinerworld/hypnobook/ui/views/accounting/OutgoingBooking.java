package net.steinerworld.hypnobook.ui.views.accounting;

import java.time.LocalDate;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;

public class OutgoingBooking extends Div {

   private final TextField belegNrField = new TextField("Beleg-Nr.");
   private final NumberField betragField = new NumberField("Betrag in CHF");
   private final DatePicker buchungsdatumField = new DatePicker("Datum");
   private final Select<Category> kategorieField = new Select<>();
   private final TextField textField = new TextField("Text");
   private final Button buchenButton = new Button("Buchen");


   public OutgoingBooking(List<Category> catList) {
      setWidthFull();

      kategorieField.setLabel("Kategorie");
      kategorieField.setItemLabelGenerator(Category::getBezeichnung);
      kategorieField.setItems(catList);

      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragField.setSuffixComponent(chfSuffix);

      FormLayout layout = new FormLayout();
      layout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("100px", 1),
            new FormLayout.ResponsiveStep("500px", 3)
      );
      layout.setColspan(textField, 2);
      layout.add(belegNrField, betragField, buchungsdatumField, kategorieField, textField);
      add(layout, buchenButton);
   }

   public OutgoingBooking withBinder(Binder<Accounting> binder) {
      binder.forField(belegNrField)
            .asRequired("Ohne Beleg-Nr. geht es nicht")
            .bind(Accounting::getBelegNr, Accounting::setBelegNr);
      binder.forField(betragField)
            .bind(Accounting::getAusgabe, Accounting::setAusgabe);
      binder.forField(buchungsdatumField)
            .withValidator(date -> inValidPeriode(date, binder.getBean()), "Buchungsdatum ist nicht in ausgewählter TaxPeriod")
            .bind(Accounting::getBuchungsdatum, Accounting::setBuchungsdatum);
      binder.forField(kategorieField)
            .bind(Accounting::getCategory, Accounting::setCategory);
      binder.forField(textField)
            .asRequired("Woher stammt die Einnahme?")
            .bind(Accounting::getText, Accounting::setText);
      return this;
   }

   private boolean inValidPeriode(LocalDate date, Accounting currAccount) {
      TaxPeriod cp = currAccount.getTaxPeriod();
      return date.isAfter(cp.getVon()) && date.isBefore(cp.getBis()) && cp.getStatus() != TaxPeriodState.GESCHLOSSEN;
   }

   public Button getBuchenButton() {
      return buchenButton;
   }
}