package net.steinerworld.hypnobook.ui.views.accounting;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.AccountingType;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;
import net.steinerworld.hypnobook.exceptions.MaloneyException;
import net.steinerworld.hypnobook.services.AccountingService;
import net.steinerworld.hypnobook.services.CategoryService;
import net.steinerworld.hypnobook.services.TaxPeriodService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Accounting")
@Route(value = "accounting", layout = MainLayout.class)
@RequiredArgsConstructor
public class AccountingView extends VerticalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(AccountingView.class);
   private final AccountingService accountingService;
   private final CategoryService categoryService;
   private final TaxPeriodService periodeService;
   private final BeanValidationBinder<Accounting> ausgabeBinder = new BeanValidationBinder<>(Accounting.class);
   private final BeanValidationBinder<Accounting> einnahmeBinder = new BeanValidationBinder<>(Accounting.class);
   private final Grid<Accounting> grid = new Grid<>();
   private final Binder<TaxPeriod> currentPeriode = new Binder<>(TaxPeriod.class);
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
      ausgabeBinder.setBean(newBuchhaltung(AccountingType.AUSGABE));
      tabContent.add(ausgabeForm, saveAusgabeButton);
      loadData();
   }

   private static ComponentRenderer<FlexLayout, TaxPeriod> createPeriodeRenderer() {
      return new ComponentRenderer<>(periode -> {
         FlexLayout wrapper = new FlexLayout();
         wrapper.setAlignItems(FlexComponent.Alignment.CENTER);

         Div info = new Div();
         info.setText(periode.getGeschaeftsjahr() + " (" + periode.getStatus() + ")");

         Div duration = new Div();
         duration.setText(periode.getVon() + " - " + periode.getBis());
         duration.getStyle().set("font-size", "var(--lumo-font-size-s)");
         duration.getStyle().set("color", "var(--lumo-secondary-text-color)");
         info.add(duration);

         wrapper.add(info);
         return wrapper;
      });
   }

   private void initializeCurrentSteuerperiode() {
      periodeService.findActive().ifPresent(currentPeriode::setBean);
      currentPeriode.addStatusChangeListener(event -> {
         LOGGER.info("load data for current {}", currentPeriode.getBean());
         loadData();
      });
   }

   private HorizontalLayout createOverview() {
      List<TaxPeriod> periodeList = periodeService.findAll();
      Select<TaxPeriod> periodeSelect = new Select<>();
      periodeSelect.setItems(periodeList);
      periodeSelect.setLabel("TaxPeriod");
      periodeSelect.setRenderer(AccountingView.createPeriodeRenderer());
      periodeSelect.setItemLabelGenerator(item -> item.getGeschaeftsjahr() + " (" + item.getStatus().name() + ")");
      periodeSelect.setValue(currentPeriode.getBean());
      periodeSelect.addValueChangeListener(event -> currentPeriode.setBean(event.getValue()));

      totalAusgabenTextField.setReadOnly(true);
      totalEinnahmenTextField.setReadOnly(true);
      return new HorizontalLayout(periodeSelect, totalAusgabenTextField, totalEinnahmenTextField);
   }

   private Grid<Accounting> createBuchhaltungGrid() {
      grid.addColumn(Accounting::getBuchungsdatum).setHeader("Datum");
      grid.addColumn(Accounting::getEinnahme).setHeader("Einnahme");
      grid.addColumn(Accounting::getAusgabe).setHeader("Ausgabe");
      grid.addColumn(Accounting::getBelegNr).setHeader("Beleg-Nr.");
      grid.addColumn(Accounting::getText).setHeader("Buchungstext");
      grid.setHeightFull();
      grid.addItemDoubleClickListener(event -> {
         if (currentPeriode.getBean().getStatus() != TaxPeriodState.GESCHLOSSEN) {
            Accounting item = event.getItem();
            if (item.getAccountingType() == AccountingType.AUSGABE) {
               tabs.setSelectedTab(ausgabeTab);
               ausgabeBinder.setBean(item);
            } else if (item.getAccountingType() == AccountingType.EINNAHME) {
               tabs.setSelectedTab(einnahmeTab);
               einnahmeBinder.setBean(item);
            } else {
               throw new MaloneyException("Weder Ausgabe noch Eingabe Typ");
            }
         } else {
            Notification.show("TaxPeriod geschlossen! Keine Bearbeitung möglich");
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
            ausgabeBinder.setBean(newBuchhaltung(AccountingType.AUSGABE));
            tabContent.add(ausgabeForm, saveAusgabeButton);
         } else if (tab.equals(einnahmeTab)) {
            einnahmeBinder.setBean(newBuchhaltung(AccountingType.EINNAHME));
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
            .bind(Accounting::getBelegNr, Accounting::setBelegNr);

      NumberField betragNumberField = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragNumberField.setSuffixComponent(chfSuffix);
      ausgabeBinder.forField(betragNumberField)
            .bind(Accounting::getAusgabe, Accounting::setAusgabe);

      DatePicker buchungsdatumDatePicker = new DatePicker("Datum");
      ausgabeBinder.forField(buchungsdatumDatePicker)
            .withValidator(this::inValidPeriode, "Buchungsdatum ist nicht in ausgewählter TaxPeriod")
            .bind(Accounting::getBuchungsdatum, Accounting::setBuchungsdatum);

      Select<Category> kategorieSelect = new Select<>();
      kategorieSelect.setLabel("Category");
      kategorieSelect.setItemLabelGenerator(Category::getBezeichnung);
      kategorieSelect.setItems(categoryService.findAll());
      ausgabeBinder.forField(kategorieSelect).bind(Accounting::getCategory, Accounting::setCategory);

      TextField textTextField = new TextField("Text");
      layout.setColspan(textTextField, 2);
      ausgabeBinder.forField(textTextField)
            .asRequired("Bitte Verwendungszweck angeben")
            .bind(Accounting::getText, Accounting::setText);

      layout.add(belegNrTextField, betragNumberField, buchungsdatumDatePicker, kategorieSelect, textTextField);
      return layout;
   }

   private FormLayout createEinnahmeForm() {
      FormLayout layout = createFormlayout();

      TextField belegNrTextField = new TextField("Beleg-Nr.");
      einnahmeBinder.forField(belegNrTextField)
            .asRequired("Ohne Beleg-Nr. geht es nicht")
            .bind(Accounting::getBelegNr, Accounting::setBelegNr);

      NumberField betragNumerField = new NumberField("Betrag in CHF");
      Div chfSuffix = new Div();
      chfSuffix.setText("CHF");
      betragNumerField.setSuffixComponent(chfSuffix);
      einnahmeBinder.forField(betragNumerField)
            .bind(Accounting::getEinnahme, Accounting::setEinnahme);

      DatePicker buchungsdatumDatePicker = new DatePicker("Datum");
      einnahmeBinder.forField(buchungsdatumDatePicker)
            .withValidator(this::inValidPeriode, "Buchungsdatum ist nicht in ausgewählter TaxPeriod")
            .bind(Accounting::getBuchungsdatum, Accounting::setBuchungsdatum);

      DatePicker zahlungseingangDatePicker = new DatePicker("Zahlungseingang");
      einnahmeBinder.forField(zahlungseingangDatePicker).bind(Accounting::getEingangsdatum, Accounting::setEingangsdatum);

      TextField textTextField = new TextField("Text");
      layout.setColspan(textTextField, 2);
      einnahmeBinder.forField(textTextField)
            .asRequired("Woher stammt die Einnahme?")
            .bind(Accounting::getText, Accounting::setText);

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
      TaxPeriod cp = currentPeriode.getBean();
      return date.isAfter(cp.getVon()) && date.isBefore(cp.getBis()) && cp.getStatus() != TaxPeriodState.GESCHLOSSEN;
   }

   private Button createAusgabeSaveButton() {
      Button button = new Button("Buchen", e -> {
         BinderValidationStatus<Accounting> validate = ausgabeBinder.validate();
         if (validate.isOk()) {
            Accounting bean = ausgabeBinder.getBean();
            bean.setTaxPeriod(getPeriodeFromNew(bean));
            saveBuchungAndRefresh(bean);
            ausgabeBinder.setBean(newBuchhaltung(bean.getAccountingType()));
         } else {
            Notification.show("Keine valide Accounting");
         }
      });
      ausgabeBinder.addStatusChangeListener(e -> button.setEnabled(ausgabeBinder.isValid()));
      return button;
   }

   private Button createEinnahmeSaveButton() {
      Button button = new Button("Buchen", e -> {
         BinderValidationStatus<Accounting> validate = einnahmeBinder.validate();
         if (validate.isOk()) {
            Accounting bean = einnahmeBinder.getBean();
            bean.setTaxPeriod(getPeriodeFromNew(bean));
            saveBuchungAndRefresh(bean);
            einnahmeBinder.setBean(newBuchhaltung(bean.getAccountingType()));
         } else {
            Notification.show("Keine valide Accounting");
         }
      });
      einnahmeBinder.addStatusChangeListener(e -> button.setEnabled(einnahmeBinder.isValid()));
      return button;
   }

   private void loadData() {
      TaxPeriod cp = currentPeriode.getBean();
      List<Accounting> list = accountingService.findAllSortedInPeriode(cp);
      grid.setItems(list);
      totalAusgabenTextField.setValue("" + accountingService.sumAusgabenInPeriode(cp));
      totalEinnahmenTextField.setValue("" + accountingService.sumEinnahmenInPeriode(cp));
      tabs.setVisible(cp.getStatus() != TaxPeriodState.GESCHLOSSEN);
      tabContent.setVisible(cp.getStatus() != TaxPeriodState.GESCHLOSSEN);
   }

   private TaxPeriod getPeriodeFromNew(Accounting item) {
      return periodeService.findAll().stream()
            .filter(periode -> item.getBuchungsdatum().isAfter(periode.getVon()))
            .filter(periode -> item.getBuchungsdatum().isBefore(periode.getBis()))
            .findFirst()
            .orElseThrow();

   }

   private void saveBuchungAndRefresh(Accounting entity) {
      accountingService.save(entity);
      loadData();
   }

   private Accounting newBuchhaltung(AccountingType type) {
      return new Accounting()
            .setAccountingType(type)
            .setBuchungsdatum(LocalDate.now(ZoneId.systemDefault()));
   }

}
