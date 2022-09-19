package net.steinerworld.hypnobook.ui.views.buchung;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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
   private final BeanValidationBinder<Buchung> ausgabeBinder = new BeanValidationBinder<>(Buchung.class);
   private final BeanValidationBinder<Buchung> einnahmeBinder = new BeanValidationBinder<>(Buchung.class);
   private final Grid<Buchung> grid = new Grid<>();
   private final Binder<Steuerperiode> currentPeriode = new Binder<>(Steuerperiode.class);
   private final TextField totalAusgabenTextField = new TextField("Total Ausgaben");
   private final TextField totalEinnahmenTextField = new TextField("Total Einnahmen");

   private final Tabs tabs = new Tabs();
   private final Tab ausgabeTab = new Tab("Ausgabe");
   private final Tab einnahmeTab = new Tab("Einnahme");
   private final VerticalLayout tabContent = new VerticalLayout();

   private FormLayout ausgabeForm;
   private FormLayout einnahmeForm;
   private Button saveAusgabeButton;
   private Button saveEinnahmeButton;



   @PostConstruct
   public void initialize() {
      setHeightFull();
      initializeCurrentSteuerperiode();

      add(createOverview());

      add(createBuchhaltungGrid());

      add(createBuchungTab(), tabContent);
      ausgabeForm = createAusgabeForm();
      einnahmeForm = createEinnahmeForm();
      saveAusgabeButton = createAusgabeSaveButton();
      saveEinnahmeButton = createEinnahmeSaveButton();

      tabs.setSelectedTab(ausgabeTab);
      ausgabeBinder.setBean(newBuchhaltung(BuchungType.AUSGABE));
      tabContent.add(ausgabeForm, saveAusgabeButton);
      loadData();
   }

   private void initializeCurrentSteuerperiode() {
      currentPeriode.setBean(periodeRepository.findByStatusEquals(SteuerperiodeState.AKTIV));
      currentPeriode.addStatusChangeListener(event -> {
         LOGGER.info("load data for current {}", currentPeriode.getBean());
         loadData();
      });
   }

   private HorizontalLayout createOverview() {
      List<Steuerperiode> periodeList = periodeRepository.findAll();
      Select<Steuerperiode> periodeSelect = new Select<>();
      periodeSelect.setItems(periodeList);
      periodeSelect.setLabel("Steuerperiode");
      periodeSelect.setRenderer(BuchungView.createPeriodeRenderer());
      periodeSelect.setItemLabelGenerator(item -> item.getJahresbezeichnung() + " (" + item.getStatus().name() + ")");
      periodeSelect.setValue(currentPeriode.getBean());
      periodeSelect.addValueChangeListener(event -> currentPeriode.setBean(event.getValue()));

      totalAusgabenTextField.setReadOnly(true);
      totalEinnahmenTextField.setReadOnly(true);
      return new HorizontalLayout(periodeSelect, totalAusgabenTextField, totalEinnahmenTextField);
   }

   private static ComponentRenderer<FlexLayout, Steuerperiode> createPeriodeRenderer() {
      return new ComponentRenderer<>(periode -> {
         FlexLayout wrapper = new FlexLayout();
         wrapper.setAlignItems(FlexComponent.Alignment.CENTER);

         Div info = new Div();
         info.setText(periode.getJahresbezeichnung() + " (" + periode.getStatus() + ")");

         Div duration = new Div();
         duration.setText(periode.getVon() + " - " + periode.getBis());
         duration.getStyle().set("font-size", "var(--lumo-font-size-s)");
         duration.getStyle().set("color", "var(--lumo-secondary-text-color)");
         info.add(duration);

         wrapper.add(info);
         return wrapper;
      });
   }

   private Grid<Buchung> createBuchhaltungGrid() {
      grid.addColumn(Buchung::getBuchungsdatum).setHeader("Datum");
      grid.addColumn(Buchung::getEinnahme).setHeader("Einnahme");
      grid.addColumn(Buchung::getAusgabe).setHeader("Ausgabe");
      grid.addColumn(Buchung::getBelegNr).setHeader("Beleg-Nr.");
      grid.addColumn(Buchung::getText).setHeader("Buchungstext");
      grid.setHeightFull();
      grid.addItemDoubleClickListener(event -> {
         if (currentPeriode.getBean().getStatus() != SteuerperiodeState.GESCHLOSSEN) {
            Buchung item = event.getItem();
            if (item.getBuchungType() == BuchungType.AUSGABE) {
               tabs.setSelectedTab(ausgabeTab);
               ausgabeBinder.setBean(item);
            } else if (item.getBuchungType() == BuchungType.EINNAHME) {
               tabs.setSelectedTab(einnahmeTab);
               einnahmeBinder.setBean(item);
            } else {
               throw new MaloneyException("Weder Ausgabe noch Eingabe Typ");
            }
         } else {
            Notification.show("Steuerperiode geschlossen! Keine Bearbeitung möglich");
         }
      });
      return grid;
   }

   private Tabs createBuchungTab() {
      tabs.add(ausgabeTab, einnahmeTab);
      tabs.addSelectedChangeListener(event -> {
         Tab tab = event.getSelectedTab();
         tabContent.removeAll();
         if (tab.equals(ausgabeTab)) {
            ausgabeBinder.setBean(newBuchhaltung(BuchungType.AUSGABE));
            tabContent.add(ausgabeForm, saveAusgabeButton);
         } else if (tab.equals(einnahmeTab)) {
            einnahmeBinder.setBean(newBuchhaltung(BuchungType.EINNAHME));
            tabContent.add(einnahmeForm, saveEinnahmeButton);
         }
      });
      return tabs;
   }

   private FormLayout createAusgabeForm() {
      FormLayout layout = createFormlayout();

      TextField belegNrTextField = new TextField("Beleg-Nr.");
      ausgabeBinder.forField(belegNrTextField)
            .asRequired("Ohne Beleg-Nr. geht es nicht")
            .bind(Buchung::getBelegNr, Buchung::setBelegNr);

      NumberField betragNumberField = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragNumberField.setSuffixComponent(chfSuffix);
      ausgabeBinder.forField(betragNumberField)
            .bind(Buchung::getAusgabe, Buchung::setAusgabe);

      DatePicker buchungsdatumDatePicker = new DatePicker("Datum");
      ausgabeBinder.forField(buchungsdatumDatePicker)
            .withValidator(this::inValidPeriode, "Buchung in geschlossener oder noch nicht erfasster Steuerperiode")
            .bind(Buchung::getBuchungsdatum, Buchung::setBuchungsdatum);

      Select<Kategorie> kategorieSelect = new Select<>();
      kategorieSelect.setLabel("Kategorie");
      kategorieSelect.setItemLabelGenerator(Kategorie::getName);
      kategorieSelect.setItems(kategorieRepository.findAll());
      ausgabeBinder.forField(kategorieSelect).bind(Buchung::getKategorie, Buchung::setKategorie);

      TextField textTextField = new TextField("Text");
      layout.setColspan(textTextField, 2);
      ausgabeBinder.forField(textTextField)
            .asRequired("Bitte Verwendungszweck angeben")
            .bind(Buchung::getText, Buchung::setText);

      layout.add(belegNrTextField, betragNumberField, buchungsdatumDatePicker, kategorieSelect, textTextField);
      return layout;
   }

   private FormLayout createEinnahmeForm() {
      FormLayout layout = createFormlayout();

      TextField belegNrTextField = new TextField("Beleg-Nr.");
      einnahmeBinder.forField(belegNrTextField)
            .asRequired("Ohne Beleg-Nr. geht es nicht")
            .bind(Buchung::getBelegNr, Buchung::setBelegNr);

      NumberField betragNumerField = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragNumerField.setSuffixComponent(chfSuffix);
      einnahmeBinder.forField(betragNumerField)
            .bind(Buchung::getEinnahme, Buchung::setEinnahme);

      DatePicker buchungsdatumDatePicker = new DatePicker("Datum");
      einnahmeBinder.forField(buchungsdatumDatePicker)
            .withValidator(this::inValidPeriode, "Das Buchungsdatum muss innerhalb der ausgewählten Steuerperiode liegen")
            .bind(Buchung::getBuchungsdatum, Buchung::setBuchungsdatum);

      DatePicker zahlungseingangDatePicker = new DatePicker("Zahlungseingang");
      einnahmeBinder.forField(zahlungseingangDatePicker).bind(Buchung::getEingangsdatum, Buchung::setEingangsdatum);

      TextField textTextField = new TextField("Text");
      layout.setColspan(textTextField, 2);
      einnahmeBinder.forField(textTextField)
            .asRequired("Woher stammt die Einnahme?")
            .bind(Buchung::getText, Buchung::setText);

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
      Steuerperiode cp = currentPeriode.getBean();
      return date.isAfter(cp.getVon()) && date.isBefore(cp.getBis()) && cp.getStatus() != SteuerperiodeState.GESCHLOSSEN;
   }

   private Button createAusgabeSaveButton() {
      Button button = new Button("Buchen", e -> {
         BinderValidationStatus<Buchung> validate = ausgabeBinder.validate();
         if (validate.isOk()) {
            Buchung bean = ausgabeBinder.getBean();
            bean.setSteuerperiode(getPeriodeFromNew(bean));
            saveBuchungAndRefresh(bean);
            ausgabeBinder.setBean(newBuchhaltung(bean.getBuchungType()));
         } else {
            Notification.show("Keine valide Buchung");
         }
      });
      ausgabeBinder.addStatusChangeListener(e -> button.setEnabled(ausgabeBinder.isValid()));
      return button;
   }

   private Button createEinnahmeSaveButton() {
      Button button = new Button("Buchen", e -> {
         BinderValidationStatus<Buchung> validate = einnahmeBinder.validate();
         if (validate.isOk()) {
            Buchung bean = einnahmeBinder.getBean();
            bean.setSteuerperiode(getPeriodeFromNew(bean));
            saveBuchungAndRefresh(bean);
            einnahmeBinder.setBean(newBuchhaltung(bean.getBuchungType()));
         } else {
            Notification.show("Keine valide Buchung");
         }
      });
      einnahmeBinder.addStatusChangeListener(e -> button.setEnabled(einnahmeBinder.isValid()));
      return button;
   }

   private void loadData() {
      Steuerperiode cp = currentPeriode.getBean();
      List<Buchung> list = buchungRepository.findBySteuerperiode(cp, Sort.by(Sort.Direction.DESC, "buchungsdatum", "id"));
      grid.setItems(list);
      double sumAusgaben = list.stream().filter(buchung -> Objects.nonNull(buchung.getAusgabe())).mapToDouble(Buchung::getAusgabe).sum();
      totalAusgabenTextField.setValue("" + sumAusgaben);
      double sumEinnahmen = list.stream().filter(buchung -> Objects.nonNull(buchung.getEinnahme())).mapToDouble(Buchung::getEinnahme).sum();
      totalEinnahmenTextField.setValue("" + sumEinnahmen);
      tabs.setVisible(cp.getStatus() != SteuerperiodeState.GESCHLOSSEN);
      tabContent.setVisible(cp.getStatus() != SteuerperiodeState.GESCHLOSSEN);
   }

   private Steuerperiode getPeriodeFromNew(Buchung item) {
      return periodeRepository.findAll().stream()
            .filter(periode -> item.getBuchungsdatum().isAfter(periode.getVon()))
            .filter(periode -> item.getBuchungsdatum().isBefore(periode.getBis()))
            .findFirst()
            .orElseThrow();

   }

   private void saveBuchungAndRefresh(Buchung entity) {
      buchungRepository.save(entity);
      loadData();
   }

   private Buchung newBuchhaltung(BuchungType type) {
      return new Buchung()
            .setBuchungType(type)
            .setBuchungsdatum(LocalDate.now(ZoneId.systemDefault()));
   }

}
