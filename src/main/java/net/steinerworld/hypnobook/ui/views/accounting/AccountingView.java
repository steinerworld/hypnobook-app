package net.steinerworld.hypnobook.ui.views.accounting;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.AccountingType;
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
   private final Binder<Accounting> outgoingBinder = new Binder<>(Accounting.class);
   private final Binder<Accounting> ingoingBinder = new Binder<>(Accounting.class);
   private final Binder<TaxPeriod> taxBinder = new Binder<>(TaxPeriod.class);

   @PostConstruct
   public void initialize() {
      setHeightFull();
      addOverview();
      addJournal();
      addBuchungTabs();
      ingoingBinder.setBean(newBuchhaltung(AccountingType.EINNAHME));
      outgoingBinder.setBean(newBuchhaltung(AccountingType.AUSGABE));
   }

   private void addOverview() {
      List<TaxPeriod> periodeList = periodeService.findAll();
      Select<TaxPeriod> periodeSelect = new Select<>();
      periodeSelect.setItems(periodeList);
      periodeSelect.setLabel("Steuerperiode");
      periodeSelect.setRenderer(AccountingView.createPeriodeRenderer());
      periodeSelect.setItemLabelGenerator(item -> item.getGeschaeftsjahr() + " (" + item.getStatus().name() + ")");
      periodeSelect.addValueChangeListener(event -> taxBinder.setBean(event.getValue()));

      TextField totalAusgabenTextField = new TextField("Total Ausgaben");
      TextField totalEinnahmenTextField = new TextField("Total Einnahmen");
      totalAusgabenTextField.setReadOnly(true);
      totalEinnahmenTextField.setReadOnly(true);
      taxBinder.addStatusChangeListener(e -> {
         TaxPeriod tax = taxBinder.getBean();
         totalAusgabenTextField.setValue("" + accountingService.sumAusgabenInPeriode(tax));
         totalEinnahmenTextField.setValue("" + accountingService.sumEinnahmenInPeriode(tax));
      });
      periodeService.findActive().ifPresent(tax -> {
         taxBinder.setBean(tax);
         periodeSelect.setValue(tax);
      });
      HorizontalLayout layout = new HorizontalLayout(periodeSelect, totalAusgabenTextField, totalEinnahmenTextField);
      add(layout);
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

   private void addJournal() {
      Journal journal = new Journal();
      journal.addItemDoubleClickListener(event -> {
         if (taxBinder.getBean().getStatus() != TaxPeriodState.GESCHLOSSEN) {
            Accounting item = event.getItem();
            if (item.getAccountingType() == AccountingType.AUSGABE) {
               outgoingBinder.setBean(item);
            } else if (item.getAccountingType() == AccountingType.EINNAHME) {
               ingoingBinder.setBean(item);
            } else {
               throw new MaloneyException("Weder Ausgabe noch Eingabe Typ");
            }
         } else {
            Notification.show("TaxPeriod geschlossen! Keine Bearbeitung möglich");
         }
      });
      taxBinder.addStatusChangeListener(e -> loadAccountingData(journal));
      ingoingBinder.addStatusChangeListener(e -> loadAccountingData(journal));
      outgoingBinder.addStatusChangeListener(e -> loadAccountingData(journal));

      add(journal);
   }

   private void loadAccountingData(Grid<Accounting> grid) {
      TaxPeriod cp = taxBinder.getBean();
      List<Accounting> list = accountingService.findAllSortedInPeriode(cp);
      grid.setItems(list);
   }

   private void addBuchungTabs() {
      IngoingBooking in = new IngoingBooking().withBinder(ingoingBinder);
      in.getBuchenButton().addClickListener(e -> saveAccounting(ingoingBinder));

      OutgoingBooking out = new OutgoingBooking(categoryService.findAll()).withBinder(outgoingBinder);
      out.getBuchenButton().addClickListener(e -> saveAccounting(outgoingBinder));

      Div content = new Div();
      Tab inTab = new Tab("Einnahmen");
      Tab outTab = new Tab("Ausgaben");
      Tabs tabs = new Tabs(inTab, outTab);
      tabs.addSelectedChangeListener(event -> {
         Tab selected = event.getSelectedTab();
         LOGGER.info("switch to {}", selected);
         content.removeAll();
         if (selected.equals(outTab)) {
            content.add(out);
         } else if (selected.equals(inTab)) {
            content.add(in);
         }
      });
      outgoingBinder.addStatusChangeListener(e -> tabs.setSelectedTab(outTab));
      ingoingBinder.addStatusChangeListener(e -> tabs.setSelectedTab(inTab));
      add(tabs, content);
   }

   private void saveAccounting(Binder binder) {
      if (binder.validate().isOk()) {
         Accounting bean = (Accounting) binder.getBean();
         accountingService.save(bean);
         binder.setBean(newBuchhaltung(bean.getAccountingType()));
         LOGGER.info("save accounting: {}", bean);
      } else {
         Notification.show("Keine valide Accounting");
      }
   }

   private Accounting newBuchhaltung(AccountingType type) {
      return new Accounting()
            .setAccountingType(type)
            .setTaxPeriod(taxBinder.getBean())
            .setBuchungsdatum(LocalDate.now(ZoneId.systemDefault()));
   }

}
