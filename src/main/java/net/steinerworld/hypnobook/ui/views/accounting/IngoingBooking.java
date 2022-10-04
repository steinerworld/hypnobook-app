package net.steinerworld.hypnobook.ui.views.accounting;

import java.time.LocalDate;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

import net.steinerworld.hypnobook.domain.Accounting;

public class IngoingBooking extends AbstractBooking<IngoingBooking> {

   private final Checkbox zahlungseingangCheck = new Checkbox("Zahlung erhalten");
   private final DatePicker zahlungsdatumField = new DatePicker("Zahlungseingang");

   public IngoingBooking() {
      super();

      FormLayout layout = createFormLayout();
      layout.add(belegNrField, betragField, buchungsdatumField, zahlungseingangCheck, zahlungsdatumField, textField);
      add(layout, buchenButton);
   }

   @Override public IngoingBooking withBinder(Binder<Accounting> binder) {
      super.withBinder(binder);
      binder.forField(betragField)
            .bind(Accounting::getEinnahme, Accounting::setEinnahme);
      binder.forField(zahlungsdatumField)
            .bind(Accounting::getEingangsdatum, Accounting::setEingangsdatum);

      binder.addStatusChangeListener(e -> {
         LocalDate date = binder.getBean().getEingangsdatum();
         zahlungseingangCheck.setValue(date != null);
         zahlungsdatumField.setEnabled(date != null);
      });

      zahlungseingangCheck.addValueChangeListener(event -> {
         boolean check = event.getValue();
         zahlungsdatumField.setEnabled(check);
         if (check) {
            zahlungsdatumField.setValue(LocalDate.now());
         } else {
            zahlungsdatumField.setValue(null);
         }
      });
      return this;
   }


}
