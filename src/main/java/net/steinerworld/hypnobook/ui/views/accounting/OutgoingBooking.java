package net.steinerworld.hypnobook.ui.views.accounting;

import java.util.List;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.Category;

public class OutgoingBooking extends CustomField<Accounting> {

   private final TextField belegNrField = new TextField("Beleg-Nr.");
   private final NumberField betragField = new NumberField("Betrag in CHF");
   private final DatePicker buchungsdatumField = new DatePicker("Datum");
   private final Select<Category> kategorieField = new Select<>();
   private final TextField textField = new TextField("Text");

   public OutgoingBooking(List<Category> catList) {
      setWidthFull();

      kategorieField.setLabel("Kategorie");
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
      add(layout);
   }

   @Override protected Accounting generateModelValue() {
      return null;
   }

   @Override protected void setPresentationValue(Accounting newPresentationValue) {

   }
}
