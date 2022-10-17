package net.steinerworld.hypnobook.ui.views.dashboard;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;
import net.steinerworld.hypnobook.services.AccountingService;
import net.steinerworld.hypnobook.services.TaxPeriodService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Übersicht")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RequiredArgsConstructor
public class DashboardView extends VerticalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(DashboardView.class);
   private static final DecimalFormat DF = new DecimalFormat("#,##0.00");
   private final TaxPeriodService taxService;
   private final AccountingService accountingService;

   private final Binder<TaxPeriod> taxBinder = new Binder<>(TaxPeriod.class);

   @PostConstruct
   public void initialize() {
      setSizeFull();
      setAlignItems(Alignment.CENTER);

      taxBinder.addStatusChangeListener(event -> {
         TaxPeriod tax = taxBinder.getBean();
         removeAll();
         addTitle(tax);
         addSummary(tax);
         addCharts(tax);
      });

      taxService.findActive().ifPresent(taxBinder::setBean);
   }

   private void addTitle(TaxPeriod tax) {
      HorizontalLayout titleLayout = new HorizontalLayout();
      titleLayout.setPadding(true);
      titleLayout.setWidthFull();
      titleLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
      titleLayout.setAlignItems(Alignment.CENTER);

      Span title = new Span("Geschäftsjahr " + tax.getGeschaeftsjahr());
      title.setClassName("overview-title");

      Button previousYear = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
      previousYear.addThemeVariants(ButtonVariant.LUMO_ICON);
      previousYear.addClickListener(event -> {
         Optional<TaxPeriod> maybePrevious = taxService.findByBusinessYear(taxBinder.getBean().getGeschaeftsjahr() - 1);
         maybePrevious.ifPresentOrElse(taxBinder::setBean, () -> Notification.show("Kein vorheriges Jahr erfasst"));
      });

      Button nextYear = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
      nextYear.addThemeVariants(ButtonVariant.LUMO_ICON);
      nextYear.addClickListener(event -> {
         Optional<TaxPeriod> maybeNext = taxService.findByBusinessYear(taxBinder.getBean().getGeschaeftsjahr() + 1);
         maybeNext.ifPresentOrElse(taxBinder::setBean, () -> Notification.show("Kein nächstes Jahr erfasst"));
      });

      titleLayout.add(previousYear, title, nextYear);
      add(titleLayout);
   }

   private void addSummary(TaxPeriod tax) {
      TaxPeriodState taxStatus = tax.getStatus();
      double ingoing = accountingService.sumEinnahmenInPeriode(tax);
      double outgoing = accountingService.sumAusgabenInPeriode(tax);
      add(new TaxSummary(taxStatus, ingoing, outgoing));
   }

   private void addCharts(TaxPeriod tax) {
      HorizontalLayout layout = new HorizontalLayout();
      layout.setSizeFull();
      layout.add(createBarChart(tax), createPieChart(tax));
      add(layout);
   }

   private VerticalLayout createBarChart(TaxPeriod tax) {
      Span subTitle = new Span("Ein- und Ausgaben pro Monat");
      subTitle.setClassName("overview-subtitle");

      List<Double> inSum = taxService.ingoingSumPerMonthByTax(tax);
      List<Double> outSum = taxService.outgoingSumPerMonthByTax(tax);
      SumPerMonthBarChart example = new SumPerMonthBarChart(inSum, outSum);
      Div div = new Div(example.build());
      div.setWidth("100%");
      VerticalLayout layout = new VerticalLayout(subTitle, div);
      layout.setAlignItems(Alignment.CENTER);
      return layout;
   }

   private VerticalLayout createPieChart(TaxPeriod tax) {
      Span subTitle = new Span("Ausgaben pro Kategorie");
      subTitle.setClassName("overview-subtitle");

      Map<String, Double> realSums = taxService.sumByTaxAndCats(tax).entrySet().stream()
            .filter(entry -> entry.getValue() > 0)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      SumPerCategoryPieChart example = new SumPerCategoryPieChart(realSums);
      Div div = new Div(example.build());
      div.setWidth("100%");
      VerticalLayout layout = new VerticalLayout(subTitle, div);
      layout.setAlignItems(Alignment.CENTER);
      return layout;
   }
}
