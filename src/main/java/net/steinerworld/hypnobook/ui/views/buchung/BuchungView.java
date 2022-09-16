package net.steinerworld.hypnobook.ui.views.buchung;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import net.steinerworld.hypnobook.domain.Buchung;
import net.steinerworld.hypnobook.domain.BuchungType;
import net.steinerworld.hypnobook.domain.Kategorie;
import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.domain.SteuerperiodeState;
import net.steinerworld.hypnobook.exceptions.MaloneyException;
import net.steinerworld.hypnobook.repository.BuchungRepository;
import net.steinerworld.hypnobook.repository.KategorieRepository;
import net.steinerworld.hypnobook.repository.SteuerperiodeRepository;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Buchung")
@Route(value = "buchung", layout = MainLayout.class)
@RequiredArgsConstructor
public class BuchungView extends VerticalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(BuchungView.class);
   private final BuchungRepository buchungRepository;
   private final KategorieRepository kategorieRepository;
   private final SteuerperiodeRepository periodeRepository;
   private final BeanValidationBinder<Buchung> buchhaltungBinder = new BeanValidationBinder<>(Buchung.class);
   private final Grid<Buchung> grid = new Grid<>();

   private Dialog ausgabeDialog;
   private Dialog einnahmeDialog;

   @PostConstruct
   public void initialize() {
      setHeightFull();
      add(createBuchhaltungGrid());

      FormLayout ausgabeForm = createBuchungForm(BuchungType.AUSGABE);
      ausgabeDialog = createBuchungDialog("Ausgabe", ausgabeForm);
      Button ausgabeButton = buildOpenDialogButton(BuchungType.AUSGABE, "Neue Ausgabe erfassen", ausgabeDialog);

      FormLayout einnahmeForm = createBuchungForm(BuchungType.EINNAHME);
      einnahmeDialog = createBuchungDialog("Einnahme", einnahmeForm);
      Button einnahmeButton = buildOpenDialogButton(BuchungType.EINNAHME, "Neue Einnahme erfassen", einnahmeDialog);

      HorizontalLayout dialogButtons = new HorizontalLayout(ausgabeButton, einnahmeButton);
      add(ausgabeDialog, einnahmeDialog, dialogButtons);
   }

   private Grid<Buchung> createBuchhaltungGrid() {
      grid.addColumn(Buchung::getBuchungsdatum).setHeader("Datum");
      grid.addColumn(Buchung::getEinnahme).setHeader("Einnahme");
      grid.addColumn(Buchung::getAusgabe).setHeader("Ausgabe");
      grid.addColumn(Buchung::getBelegNr).setHeader("Beleg-Nr.");
      grid.addColumn(Buchung::getText).setHeader("Buchungstext");
      grid.setHeightFull();
      grid.addItemDoubleClickListener(event -> {
         Buchung item = event.getItem();
         buchhaltungBinder.setBean(item);
         if (item.getBuchungType() == BuchungType.AUSGABE) {
            ausgabeDialog.open();
         } else if (item.getBuchungType() == BuchungType.EINNAHME) {
            einnahmeDialog.open();
         } else {
            throw new MaloneyException("Weder Ausgabe noch Eingabe Typ");
         }


         LOGGER.info("???????? {}", event);
      });
      loadGridData();
      return grid;
   }

   private Button buildOpenDialogButton(BuchungType type, String text, Dialog dialog) {
      return new Button(text, e -> {
         buchhaltungBinder.setBean(createNewBuchhaltung(type));
         dialog.open();
      });
   }

   private void loadGridData() {
      grid.setItems(buchungRepository.findAll(Sort.by(Sort.Direction.DESC, "buchungsdatum", "id")));
   }

   private FormLayout createBuchungForm(BuchungType type) {
      FormLayout layout = new FormLayout();
      layout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 4)
      );
      if (type == BuchungType.AUSGABE) {
         layout.add(buildSteuerperiodeComponent(),
               buildBelegNrComponent(),
               buildBetragAusgabeComponent(),
               buildBuchungsdatumComponent(),
               buildKategorieComponent(),
               buildTextComponent(layout));
      } else if (type == BuchungType.EINNAHME) {
         layout.add(buildSteuerperiodeComponent(),
               buildBelegNrComponent(),
               buildBetragEinnahmeComponent(),
               buildBuchungsdatumComponent(),
               buildZahlungseingangComponent(),
               buildTextComponent(layout));
      }
      return layout;
   }

   private Dialog createBuchungDialog(String title, FormLayout form) {
      Dialog dialog = new Dialog();
      dialog.setModal(true);
      dialog.setHeaderTitle(title);
      dialog.add(form);
      Button saveButton = new Button("Buchen", e -> {
         buchungRepository.save(buchhaltungBinder.getBean());
         dialog.close();
         loadGridData();
      });
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      Button cancelButton = new Button("Cancel", e -> dialog.close());
      dialog.getFooter().add(cancelButton);
      dialog.getFooter().add(saveButton);
      return dialog;
   }

   private TextField buildTextComponent(FormLayout layout) {
      TextField tfText = new TextField("Text");
      layout.setColspan(tfText, 2);
      buchhaltungBinder.forField(tfText).bind(Buchung::getText, Buchung::setText);
      return tfText;
   }

   private Select<Kategorie> buildKategorieComponent() {
      Select<Kategorie> seKategorie = new Select<>();
      seKategorie.setLabel("Kategorie");
      seKategorie.setItemLabelGenerator(Kategorie::getName);
      seKategorie.setItems(kategorieRepository.findAll());
      buchhaltungBinder.forField(seKategorie).bind(Buchung::getKategorie, Buchung::setKategorie);
      return seKategorie;
   }

   private DatePicker buildBuchungsdatumComponent() {
      DatePicker dpBuchungsdatum = new DatePicker("Datum");
      buchhaltungBinder.forField(dpBuchungsdatum).bind(Buchung::getBuchungsdatum, Buchung::setBuchungsdatum);
      return dpBuchungsdatum;
   }

   private NumberField buildBetragAusgabeComponent() {
      NumberField nfBetrag = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      nfBetrag.setSuffixComponent(chfSuffix);
      buchhaltungBinder.forField(nfBetrag).bind(Buchung::getAusgabe, Buchung::setAusgabe);
      return nfBetrag;
   }

   private NumberField buildBetragEinnahmeComponent() {
      NumberField nfBetrag = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      nfBetrag.setSuffixComponent(chfSuffix);
      buchhaltungBinder.forField(nfBetrag).bind(Buchung::getEinnahme, Buchung::setEinnahme);
      return nfBetrag;
   }

   private TextField buildBelegNrComponent() {
      TextField tfBelegNr = new TextField("Beleg-Nr.");
      buchhaltungBinder.forField(tfBelegNr).bind(Buchung::getBelegNr, Buchung::setBelegNr);
      return tfBelegNr;
   }

   private Select<Steuerperiode> buildSteuerperiodeComponent() {
      Select<Steuerperiode> sePeriode = new Select<>();
      sePeriode.setLabel("Steuerperiode");
      sePeriode.setItemLabelGenerator(Steuerperiode::getJahresbezeichnung);
      sePeriode.setItems(periodeRepository.findAll());
      buchhaltungBinder.forField(sePeriode).bind(Buchung::getSteuerperiode, Buchung::setSteuerperiode);
      return sePeriode;
   }

   private DatePicker buildZahlungseingangComponent() {
      DatePicker dpZahlungseingang = new DatePicker("Zahlungseingang");
      buchhaltungBinder.forField(dpZahlungseingang).bind(Buchung::getEingangsdatum, Buchung::setEingangsdatum);
      return dpZahlungseingang;
   }

   private Buchung createNewBuchhaltung(BuchungType type) {
      return new Buchung()
            .setBuchungType(type)
            .setSteuerperiode(periodeRepository.findByStatusEquals(SteuerperiodeState.AKTIV))
            .setBuchungsdatum(LocalDate.now(ZoneId.systemDefault()));
   }

}
