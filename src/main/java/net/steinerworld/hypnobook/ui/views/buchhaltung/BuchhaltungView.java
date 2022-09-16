package net.steinerworld.hypnobook.ui.views.buchhaltung;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Buchhaltung;
import net.steinerworld.hypnobook.domain.Kategorie;
import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.domain.SteuerperiodeState;
import net.steinerworld.hypnobook.repository.BuchhaltungRepository;
import net.steinerworld.hypnobook.repository.KategorieRepository;
import net.steinerworld.hypnobook.repository.SteuerperiodeRepository;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Buchhaltung")
@Route(value = "buchhaltung", layout = MainLayout.class)
@RequiredArgsConstructor
public class BuchhaltungView extends VerticalLayout {

   private final BuchhaltungRepository buchhaltungRepository;
   private final KategorieRepository kategorieRepository;
   private final SteuerperiodeRepository periodeRepository;
   private final BeanValidationBinder<Buchhaltung> buchhaltungBinder = new BeanValidationBinder<>(Buchhaltung.class);
   private final Grid<Buchhaltung> grid = new Grid<>();

   @PostConstruct
   public void initialize() {
      setHeightFull();
      add(createBuchhaltungGrid());

      Dialog ausgabeDialog = createAusgabeDialog();
      Button newAusgabeButton = new Button("Neue Ausgabe erfassen", e -> {
         buchhaltungBinder.setBean(createNewBuchhaltung());
         ausgabeDialog.open();
      });

      Dialog eingangDialog = createEingangDialog();
      Button newEingangButton = new Button("Neuen Eingang erfassen", e -> {
         buchhaltungBinder.setBean(createNewBuchhaltung());
         eingangDialog.open();
      });
      HorizontalLayout dialogButtons = new HorizontalLayout(newAusgabeButton, newEingangButton);
      add(ausgabeDialog, dialogButtons);
   }

   private Grid<Buchhaltung> createBuchhaltungGrid() {
      grid.addColumn(Buchhaltung::getBuchungsdatum).setHeader("Datum");
      grid.addColumn(Buchhaltung::getEinnahme).setHeader("Einnahme");
      grid.addColumn(Buchhaltung::getAusgabe).setHeader("Ausgabe");
      grid.addColumn(Buchhaltung::getBelegNr).setHeader("Beleg-Nr.");
      grid.addColumn(Buchhaltung::getText).setHeader("Buchungstext");
      grid.setHeightFull();
      loadGridData();
      return grid;
   }

   private void loadGridData() {
      grid.setItems(buchhaltungRepository.findAll(Sort.by(Sort.Direction.DESC, "buchungsdatum", "id")));
   }

   private Dialog createAusgabeDialog() {
      Dialog ausgabeDialog = new Dialog();
      ausgabeDialog.setModal(true);
      ausgabeDialog.setHeaderTitle("Neue Ausgabe erfassen");


      ausgabeDialog.add(createAusgabeForm());

      Button saveButton = new Button("Speichern", e -> {
         buchhaltungRepository.save(buchhaltungBinder.getBean());
         ausgabeDialog.close();
         loadGridData();
      });
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

      Button cancelButton = new Button("Cancel", e -> ausgabeDialog.close());
      ausgabeDialog.getFooter().add(cancelButton);
      ausgabeDialog.getFooter().add(saveButton);
      return ausgabeDialog;
   }

   private Dialog createEingangDialog() {
      Dialog dialog = new Dialog();
      dialog.setModal(true);
      dialog.setHeaderTitle("Neuen Eingang erfassen");


      dialog.add(createEingangForm());

      Button saveButton = new Button("Speichern", e -> {
         buchhaltungRepository.save(buchhaltungBinder.getBean());
         dialog.close();
         loadGridData();
      });
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

      Button cancelButton = new Button("Cancel", e -> dialog.close());
      dialog.getFooter().add(cancelButton);
      dialog.getFooter().add(saveButton);
      return dialog;
   }

   private FormLayout createAusgabeForm() {
      FormLayout layout = new FormLayout();
      layout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 4)
      );
      Select<Steuerperiode> sePeriode = new Select<>();
      sePeriode.setLabel("Steuerperiode");
      sePeriode.setItemLabelGenerator(Steuerperiode::getJahresbezeichnung);
      sePeriode.setItems(periodeRepository.findAll());
      buchhaltungBinder.forField(sePeriode).bind(Buchhaltung::getSteuerperiode, Buchhaltung::setSteuerperiode);

      TextField tfBelegNr = new TextField("Beleg-Nr.");
      buchhaltungBinder.forField(tfBelegNr).bind(Buchhaltung::getBelegNr, Buchhaltung::setBelegNr);

      NumberField nfBetrag = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      nfBetrag.setSuffixComponent(chfSuffix);
      buchhaltungBinder.forField(nfBetrag).bind(Buchhaltung::getAusgabe, Buchhaltung::setAusgabe);

      DatePicker dpBuchungsdatum = new DatePicker("Datum");
      buchhaltungBinder.forField(dpBuchungsdatum).bind(Buchhaltung::getBuchungsdatum, Buchhaltung::setBuchungsdatum);

      Select<Kategorie> seKategorie = new Select<>();
      seKategorie.setLabel("Kategorie");
      seKategorie.setItemLabelGenerator(Kategorie::getName);
      seKategorie.setItems(kategorieRepository.findAll());
      buchhaltungBinder.forField(seKategorie).bind(Buchhaltung::getKategorie, Buchhaltung::setKategorie);

      TextField tfText = new TextField("Text");
      layout.setColspan(tfText, 2);
      buchhaltungBinder.forField(tfText).bind(Buchhaltung::getText, Buchhaltung::setText);

      layout.add(sePeriode, tfBelegNr, nfBetrag, dpBuchungsdatum, seKategorie, tfText);
      return layout;
   }

   private FormLayout createEingangForm() {
      FormLayout layout = new FormLayout();
      layout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 4)
      );
      Select<Steuerperiode> sePeriode = new Select<>();
      sePeriode.setLabel("Steuerperiode");
      sePeriode.setItemLabelGenerator(Steuerperiode::getJahresbezeichnung);
      sePeriode.setItems(periodeRepository.findAll());
      buchhaltungBinder.forField(sePeriode).bind(Buchhaltung::getSteuerperiode, Buchhaltung::setSteuerperiode);

      TextField tfBelegNr = new TextField("Beleg-Nr.");
      buchhaltungBinder.forField(tfBelegNr).bind(Buchhaltung::getBelegNr, Buchhaltung::setBelegNr);

      NumberField nfBetrag = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      nfBetrag.setSuffixComponent(chfSuffix);
      buchhaltungBinder.forField(nfBetrag).bind(Buchhaltung::getEinnahme, Buchhaltung::setEinnahme);

      DatePicker dpBuchungsdatum = new DatePicker("Datum");
      buchhaltungBinder.forField(dpBuchungsdatum).bind(Buchhaltung::getBuchungsdatum, Buchhaltung::setBuchungsdatum);

      DatePicker dpZahlungseingang = new DatePicker("Zahlungseingang");
      buchhaltungBinder.forField(dpZahlungseingang).bind(Buchhaltung::getEingangsdatum, Buchhaltung::setEingangsdatum);

      TextField tfText = new TextField("Text");
      layout.setColspan(tfText, 2);
      buchhaltungBinder.forField(tfText).bind(Buchhaltung::getText, Buchhaltung::setText);

      layout.add(sePeriode, tfBelegNr, nfBetrag, dpBuchungsdatum, dpZahlungseingang, tfText);
      return layout;
   }

   private Buchhaltung createNewBuchhaltung() {
      return new Buchhaltung()
            .setSteuerperiode(periodeRepository.findByStatusEquals(SteuerperiodeState.AKTIV))
            .setBuchungsdatum(LocalDate.now(ZoneId.systemDefault()));
   }

}
