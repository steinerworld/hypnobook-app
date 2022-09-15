package net.steinerworld.hypnobook.ui.components.buchhaltung;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import net.steinerworld.hypnobook.domain.Kategorie;

public class AusgabeForm extends FormLayout {

   private TextField tfBelegNr;
   private NumberField nfBetrag;
   private DatePicker dpBuchungsdatum;
   private Select<Kategorie> seKategorie;
   private TextField tfText;

   public AusgabeForm() {
      setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("500px", 3)
      );

      tfBelegNr = new TextField("Beleg-Nr.");

      nfBetrag = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      nfBetrag.setSuffixComponent(chfSuffix);

      dpBuchungsdatum = new DatePicker("Datum");

      seKategorie = new Select<>();
      seKategorie.setLabel("Kategorie");

      tfText = new TextField("Text");
      setColspan(tfText, 2);

      add(tfBelegNr, nfBetrag, dpBuchungsdatum, seKategorie, tfText);

   }


}
