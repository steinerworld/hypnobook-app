package net.steinerworld.hypnobook.ui.views.accounting;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import net.steinerworld.hypnobook.domain.Accounting;

public class IngoingBooking extends CustomField<Accounting> {

   private final TextField belegNrField = new TextField("Beleg-Nr.");
   private final NumberField betragField = new NumberField("Betrag in CHF");
   private final DatePicker buchungsdatumField = new DatePicker("Datum");
   private final DatePicker zahlungsdatumField = new DatePicker("Zahlungseingang");
   private final TextField textField = new TextField("Text");

   public IngoingBooking() {
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragField.setSuffixComponent(chfSuffix);

      FormLayout layout = new FormLayout(belegNrField, betragField, buchungsdatumField, zahlungsdatumField, textField);
      add(layout);
   }

   @Override protected Accounting generateModelValue() {
      return new Accounting()
            .setBelegNr(belegNrField.getValue())
            .setEinnahme(betragField.getValue())
            .setBuchungsdatum(buchungsdatumField.getValue())
            .setEingangsdatum(zahlungsdatumField.getValue())
            .setText(textField.getValue());
   }

   @Override protected void setPresentationValue(Accounting presentationValue) {
      belegNrField.setValue(presentationValue.getBelegNr());
      betragField.setValue(presentationValue.getEinnahme());
      buchungsdatumField.setValue(presentationValue.getBuchungsdatum());
      zahlungsdatumField.setValue(presentationValue.getEingangsdatum());
      textField.setValue(presentationValue.getText());
   }
}
