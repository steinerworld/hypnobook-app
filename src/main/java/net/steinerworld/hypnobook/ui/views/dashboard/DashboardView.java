package net.steinerworld.hypnobook.ui.views.dashboard;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Anchor;
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
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;
import net.steinerworld.hypnobook.dto.JournalDto;
import net.steinerworld.hypnobook.services.AccountingService;
import net.steinerworld.hypnobook.services.JournalSheetService;
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
   private final JournalSheetService journalSheetService;

   private final Binder<TaxPeriod> taxBinder = new Binder<>(TaxPeriod.class);

   @PostConstruct
   public void initialize() {
      setSizeFull();
      setAlignItems(Alignment.CENTER);

      taxBinder.addStatusChangeListener(event -> {
         removeAll();
         TaxPeriod tax = taxBinder.getBean();
         addTitle(tax);
         addSummary(tax);
         addCharts(tax);
         addActionButtons(tax);
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
      HorizontalLayout titleLayout = new HorizontalLayout();
      titleLayout.setPadding(true);
      titleLayout.setWidthFull();
      titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
      titleLayout.setAlignItems(Alignment.CENTER);

      // Total Einnahmen
      double ingoing = accountingService.sumEinnahmenInPeriode(tax);
      Span inBadge = new Span(createBadgeIcon(VaadinIcon.ARROW_RIGHT), new Span(DF.format(ingoing)));
      inBadge.getElement().getThemeList().add("badge success");

      // Total Ausgaben
      double outgoing = accountingService.sumAusgabenInPeriode(tax);
      Span outBadge = new Span(createBadgeIcon(VaadinIcon.ARROW_LEFT), new Span(DF.format(outgoing)));
      outBadge.getElement().getThemeList().add("badge error");

      // Differenz
      double diff = ingoing - outgoing;
      Span diffBadge = new Span();
      if (diff < 0) {
         diffBadge.add(createBadgeIcon(VaadinIcon.ARROW_DOWN), new Span(DF.format(diff)));
         diffBadge.getElement().getThemeList().add("badge error primary");
      } else if (diff > 0) {
         diffBadge.add(createBadgeIcon(VaadinIcon.ARROW_UP), new Span(DF.format(diff)));
         diffBadge.getElement().getThemeList().add("badge success primary");
      } else {
         diffBadge.add(createBadgeIcon(VaadinIcon.CIRCLE_THIN), new Span(DF.format(diff)));
         diffBadge.getElement().getThemeList().add("badge primary");
      }

      // Status der Steuerperiode
      TaxPeriodState taxStatus = tax.getStatus();
      Span taxStatusBadge = new Span(taxStatus.getCaption());
      if (taxStatus == TaxPeriodState.GESCHLOSSEN) {
         taxStatusBadge.getElement().getThemeList().add("badge error");
      } else if (taxStatus == TaxPeriodState.AKTIV) {
         taxStatusBadge.getElement().getThemeList().add("badge success");
      } else {
         taxStatusBadge.getElement().getThemeList().add("badge");
      }

      titleLayout.add(inBadge, outBadge, diffBadge, taxStatusBadge);
      add(titleLayout);
   }

   private Icon createBadgeIcon(VaadinIcon vaadinIcon) {
      Icon icon = vaadinIcon.create();
      icon.getStyle().set("padding", "var(--lumo-space-xs");
      return icon;
   }

   private void addCharts(TaxPeriod tax) {
      HorizontalLayout layout = new HorizontalLayout();
      layout.setWidthFull();
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

   private void addActionButtons(TaxPeriod tax) {
      HorizontalLayout layout = new HorizontalLayout();

      Button activateTaxButton = new Button("Aktivieren", e -> confirmChangeActiveTaxperiode(taxBinder.getBean()));
      activateTaxButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

      Anchor balancePdf = buildAnchorForBalanceSheet();
      Anchor journalPdf = buildAnchorForJournalSheet();

      activateTaxButton.setVisible(tax.getStatus() != TaxPeriodState.AKTIV);
      balancePdf.setVisible(tax.getStatus() == TaxPeriodState.GESCHLOSSEN);

      layout.add(activateTaxButton, balancePdf, journalPdf);
      add(layout);
   }

   private Anchor buildAnchorForBalanceSheet() {
      String fileName = String.format("Jahresabschluss_%d.pdf", taxBinder.getBean().getGeschaeftsjahr());
      Anchor anchor = new Anchor(new StreamResource(fileName, (InputStreamFactory) () -> {
         ByteArrayOutputStream os = taxService.streamBalanceSheet(taxBinder.getBean());
         return new ByteArrayInputStream(os.toByteArray());
      }), "");
      anchor.getElement().setAttribute("download", true);
      anchor.add(new Button("Download Jahresabschluss"));
      return anchor;
   }

   private Anchor buildAnchorForJournalSheet() {
      String fileName = String.format("Journal_%d.pdf", taxBinder.getBean().getGeschaeftsjahr());
      Anchor anchor = new Anchor(new StreamResource(fileName, (InputStreamFactory) () -> {
         JournalDto dto = journalSheetService.createDto(taxBinder.getBean());
         ByteArrayOutputStream os = journalSheetService.streamJournalPDF(dto);
         return new ByteArrayInputStream(os.toByteArray());
      }), "");
      anchor.getElement().setAttribute("download", true);
      anchor.add(new Button("Download Journal"));
      return anchor;
   }

   private void confirmChangeActiveTaxperiode(TaxPeriod periode) {
      Optional<TaxPeriod> maybeActive = taxService.findActive();
      if (maybeActive.isPresent()) {
         TaxPeriod active = maybeActive.get();
         ConfirmDialog dialog = new ConfirmDialog();
         dialog.setHeader("Aktivierung der Steuerperiode " + periode.getGeschaeftsjahr());
         dialog.setText("Die aktuell aktive Steuerperiode '" + active.getGeschaeftsjahr() + "' wird geschlossen.");

         dialog.setCancelable(true);
         dialog.setConfirmText("Aktivieren");
         dialog.setConfirmButtonTheme("error primary");
         dialog.addConfirmListener(e -> {
            taxService.changeActiveTaxPeriod(active, periode);
            taxService.findActive().ifPresent(taxBinder::setBean);
         });
         dialog.open();
      } else {
         LOGGER.info("Keine aktive Steuerperiode gefunden -> einfach machen");
      }
   }

}
