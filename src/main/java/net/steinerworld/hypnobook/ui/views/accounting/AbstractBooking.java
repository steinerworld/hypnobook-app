package net.steinerworld.hypnobook.ui.views.accounting;

import java.time.LocalDate;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;

public abstract class AbstractBooking<T> extends Div {

   protected final TextField belegNrField = new TextField("Beleg-Nr.");
   protected final NumberField betragField = new NumberField("Betrag in CHF");
   protected final DatePicker buchungsdatumField = new DatePicker("Datum");
   protected final TextField textField = new TextField("Text");
   protected final Button buchenButton = new Button("Buchen");
   protected final Button cancelButton = new Button("Abbrechen");

   public AbstractBooking() {
      setWidthFull();
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragField.setSuffixComponent(chfSuffix);

      buchenButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
   }

   public T withBinder(Binder<Accounting> binder) {
      binder.forField(belegNrField)
            .asRequired("Ohne Beleg-Nr. geht es nicht")
            .bind(Accounting::getBelegNr, Accounting::setBelegNr);
      binder.forField(buchungsdatumField)
            .withValidator(date -> inValidPeriode(date, binder.getBean()), "Buchungsdatum ist nicht in ausgewählter Steuerperiode")
            .bind(Accounting::getBuchungsdatum, Accounting::setBuchungsdatum);
      binder.forField(textField)
            .asRequired("Woher stammt die Einnahme?")
            .bind(Accounting::getText, Accounting::setText);
      return null;
   }

   private static boolean inValidPeriode(LocalDate date, Accounting currAccount) {
      TaxPeriod cp = currAccount.getTaxPeriod();
      return date.isAfter(cp.getVon()) && date.isBefore(cp.getBis()) && cp.getStatus() != TaxPeriodState.GESCHLOSSEN;
   }

   public Button getBuchenButton() {
      return buchenButton;
   }

   public Button getCancelButton() {
      return cancelButton;
   }

   protected void addFormLayoutAndButtons(Component[] components) {
      FormLayout layout = createFormLayout();
      layout.add(components);
      HorizontalLayout buttons = new HorizontalLayout(buchenButton, cancelButton);
      add(layout, buttons);
   }

   private FormLayout createFormLayout() {
      FormLayout layout = new FormLayout();
      layout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("100px", 1),
            new FormLayout.ResponsiveStep("500px", 4)
      );
      layout.setColspan(textField, 3);
      return layout;
   }

}
