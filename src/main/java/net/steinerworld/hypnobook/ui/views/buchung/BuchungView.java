package net.steinerworld.hypnobook.ui.views.buchung;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
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
   private final BeanValidationBinder<Buchung> buchungBinder = new BeanValidationBinder<>(Buchung.class);
   private final Grid<Buchung> grid = new Grid<>();

   private final VerticalLayout tabContent = new VerticalLayout();
   private final Tab ausgabeTab = new Tab("Ausgabe");
   private final Tab einnahmeTab = new Tab("Einnahme");


   private Dialog ausgabeDialog;
   private Dialog einnahmeDialog;

   @PostConstruct
   public void initialize() {
      setHeightFull();

      add(createBuchhaltungGrid());

      add(createBuchungTab(), tabContent);

      //      BuchungEditForm ausgabeForm = BuchungEditForm.builder().kategorieList(kategorieRepository::findAll)
      //            .periodeList(periodeRepository::findAll)
      //            .binder(buchungBinder)
      //            .type(BuchungType.AUSGABE)
      //            .build().get();
      //      ausgabeDialog = createBuchungDialog("Ausgabe", ausgabeForm);
      //      Button ausgabeButton = buildOpenDialogButton(BuchungType.AUSGABE, "Neue Ausgabe erfassen", ausgabeDialog);
      //
      //      BuchungEditForm einnahmeForm = BuchungEditForm.builder().kategorieList(kategorieRepository::findAll)
      //            .periodeList(periodeRepository::findAll)
      //            .binder(buchungBinder)
      //            .type(BuchungType.EINNAHME)
      //            .build().get();
      //      einnahmeDialog = createBuchungDialog("Einnahme", einnahmeForm);
      //      Button einnahmeButton = buildOpenDialogButton(BuchungType.EINNAHME, "Neue Einnahme erfassen", einnahmeDialog);
      //
      //      HorizontalLayout dialogButtons = new HorizontalLayout(ausgabeButton, einnahmeButton);
      //      add(ausgabeDialog, einnahmeDialog, dialogButtons);
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
         buchungBinder.setBean(item);
         if (item.getBuchungType() == BuchungType.AUSGABE) {
            ausgabeDialog.open();
         } else if (item.getBuchungType() == BuchungType.EINNAHME) {
            einnahmeDialog.open();
         } else {
            throw new MaloneyException("Weder Ausgabe noch Eingabe Typ");
         }
      });
      loadGridData();
      return grid;
   }

   private Tabs createBuchungTab() {
      Tabs tabs = new Tabs(ausgabeTab, einnahmeTab);
      tabs.addSelectedChangeListener(event ->
            setTabContent(event.getSelectedTab())
      );
      return tabs;
   }

   private void setTabContent(Tab tab) {
      tabContent.removeAll();

      if (tab.equals(ausgabeTab)) {
         tabContent.add(createAusgabeForm(), createSaveButton());
      } else if (tab.equals(einnahmeTab)) {
         tabContent.add(createEinnahmeForm());
      }
   }

   private FormLayout createAusgabeForm() {
      FormLayout layout = createFormlayout();

      TextField belegNrTextField = new TextField("Beleg-Nr.");
      buchungBinder.forField(belegNrTextField).bind(Buchung::getBelegNr, Buchung::setBelegNr);

      NumberField betragNumberField = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragNumberField.setSuffixComponent(chfSuffix);
      buchungBinder.forField(betragNumberField).bind(Buchung::getAusgabe, Buchung::setAusgabe);

      DatePicker buchungsdatumDatePicker = new DatePicker("Datum");
      buchungBinder.forField(buchungsdatumDatePicker)
            .withValidator(this::inValidPeriode, "Buchung in geschlossener oder noch nicht erfasster Steuerperiode")
            .bind(Buchung::getBuchungsdatum, Buchung::setBuchungsdatum);

      Select<Kategorie> kategorieSelect = new Select<>();
      kategorieSelect.setLabel("Kategorie");
      kategorieSelect.setItemLabelGenerator(Kategorie::getName);
      kategorieSelect.setItems(kategorieRepository.findAll());
      buchungBinder.forField(kategorieSelect).bind(Buchung::getKategorie, Buchung::setKategorie);

      TextField textTextField = new TextField("Text");
      layout.setColspan(textTextField, 2);
      buchungBinder.forField(textTextField).bind(Buchung::getText, Buchung::setText);

      layout.add(belegNrTextField, betragNumberField, buchungsdatumDatePicker, kategorieSelect, textTextField);
      return layout;
   }

   private FormLayout createEinnahmeForm() {
      FormLayout layout = createFormlayout();

      TextField belegNrTextField = new TextField("Beleg-Nr.");
      buchungBinder.forField(belegNrTextField).bind(Buchung::getBelegNr, Buchung::setBelegNr);

      NumberField betragNumerField = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragNumerField.setSuffixComponent(chfSuffix);
      buchungBinder.forField(betragNumerField).bind(Buchung::getEinnahme, Buchung::setEinnahme);

      DatePicker buchungsdatumDatePicker = new DatePicker("Datum");
      buchungBinder.forField(buchungsdatumDatePicker)
            .withValidator(this::inValidPeriode, "Buchung in geschlossener oder noch nicht erfasster Steuerperiode")
            .bind(Buchung::getBuchungsdatum, Buchung::setBuchungsdatum);

      DatePicker zahlungseingangDatePicker = new DatePicker("Zahlungseingang");
      buchungBinder.forField(zahlungseingangDatePicker).bind(Buchung::getEingangsdatum, Buchung::setEingangsdatum);

      TextField textTextField = new TextField("Text");
      layout.setColspan(textTextField, 2);
      buchungBinder.forField(textTextField).bind(Buchung::getText, Buchung::setText);

      layout.add(belegNrTextField, betragNumerField, buchungsdatumDatePicker, zahlungseingangDatePicker, textTextField);
      return layout;
   }

   private FormLayout createFormlayout() {
      FormLayout layout = new FormLayout();
      layout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 3)
      );
      return layout;
   }

   private boolean inValidPeriode(LocalDate date) {
      return periodeRepository.findAll().stream()
            .filter(periode -> periode.getStatus() != SteuerperiodeState.GESCHLOSSEN)
            .filter(periode -> date.isAfter(periode.getVon()))
            .anyMatch(periode -> date.isBefore(periode.getBis()));
   }

   private Button createSaveButton() {
      return new Button("Buchen", e -> {
         BinderValidationStatus<Buchung> validate = buchungBinder.validate();
         if (validate.isOk()) {
            Buchung bean = buchungBinder.getBean();
            bean.setSteuerperiode(getPeriodeFromNew(bean));
            saveBuchungAndRefresh(bean);
         } else {
            Notification.show("Keine valide Buchung");
         }
      });
   }


   //   private Button buildOpenDialogButton(BuchungType type, String text, Dialog dialog) {
   //      return new Button(text, e -> {
   //         buchungBinder.setBean(newBuchhaltung(type));
   //         dialog.open();
   //      });
   //   }

   private void loadGridData() {
      grid.setItems(buchungRepository.findAll(Sort.by(Sort.Direction.DESC, "buchungsdatum", "id")));
   }

   //   private Dialog createBuchungDialog(String title, FormLayout form) {
   //      Dialog dialog = new Dialog();
   //      dialog.setModal(true);
   //      dialog.setHeaderTitle(title);
   //      dialog.add(form);
   //      Button saveButton = new Button("Buchen", e -> {
   //         BinderValidationStatus<Buchung> validate = buchungBinder.validate();
   //         if (validate.isOk()) {
   //            Buchung bean = buchungBinder.getBean();
   //            bean.setSteuerperiode(getPeriodeFromNew(bean));
   //            saveBuchungAndRefresh(bean);
   //            dialog.close();
   //         } else {
   //            Notification.show("Keine valide Buchung");
   //         }
   //      });
   //      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
   //      Button cancelButton = new Button("Cancel", e -> dialog.close());
   //      dialog.getFooter().add(cancelButton);
   //      dialog.getFooter().add(saveButton);
   //      return dialog;
   //   }

   private Steuerperiode getPeriodeFromNew(Buchung item) {
      return periodeRepository.findAll().stream()
            .filter(periode -> item.getBuchungsdatum().isAfter(periode.getVon()))
            .filter(periode -> item.getBuchungsdatum().isBefore(periode.getBis()))
            .findFirst()
            .orElseThrow();

   }

   private void saveBuchungAndRefresh(Buchung entity) {
      buchungRepository.save(entity);
      loadGridData();
   }

   private Buchung newBuchhaltung(BuchungType type) {
      return new Buchung()
            .setBuchungType(type)
            .setSteuerperiode(periodeRepository.findByStatusEquals(SteuerperiodeState.AKTIV))
            .setBuchungsdatum(LocalDate.now(ZoneId.systemDefault()));
   }

}
