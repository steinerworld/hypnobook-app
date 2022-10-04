package net.steinerworld.hypnobook.ui.views.accounting;

import java.util.List;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.Category;

public class OutgoingBooking extends AbstractBooking<OutgoingBooking> {

   private final Select<Category> kategorieField = new Select<>();

   public OutgoingBooking(List<Category> catList) {
      super();

      kategorieField.setLabel("Kategorie");
      kategorieField.setItemLabelGenerator(Category::getBezeichnung);
      kategorieField.setItems(catList);

      FormLayout layout = createFormLayout();
      layout.add(belegNrField, betragField, buchungsdatumField, kategorieField, textField);
      add(layout, buchenButton);
   }

   @Override public OutgoingBooking withBinder(Binder<Accounting> binder) {
      super.withBinder(binder);
      binder.forField(betragField)
            .bind(Accounting::getAusgabe, Accounting::setAusgabe);
      binder.forField(kategorieField)
            .bind(Accounting::getCategory, Accounting::setCategory);
      return this;
   }

}