package net.steinerworld.hypnobook.ui.views.dashboard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.services.TaxPeriodService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Übersicht")
@Route(value = "dashboard", layout = MainLayout.class)
@RequiredArgsConstructor
public class DashboardView extends VerticalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(DashboardView.class);
   private final TaxPeriodService taxService;

   @PostConstruct
   public void initialize() {
      LOGGER.info(">>>> CHART");
      setSizeFull();

      taxService.findActive().ifPresent(tax -> {
         addBarChart(tax);
         addPieChart(tax);
      });
   }

   private void addBarChart(TaxPeriod tax) {
      LOGGER.info("create inOutBar for {}", tax);
      List<Double> inSum = taxService.ingoingSumPerMonthByTax(tax);
      List<Double> outSum = taxService.outgoingSumPerMonthByTax(tax);
      SumPerMonthBarChart example = new SumPerMonthBarChart(inSum, outSum);
      Div div = new Div(example.build());
      div.setWidth("50%");
      add(div);
   }

   private void addPieChart(TaxPeriod tax) {
      LOGGER.info("create CatPie for {}", tax);
      Map<String, Double> realSums = taxService.sumByTaxAndCats(tax).entrySet().stream()
            .filter(entry -> entry.getValue() > 0)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      SumPerCategoryPieChart example = new SumPerCategoryPieChart(realSums);
      Div div = new Div(example.build());
      div.setWidth("50%");
      add(div);
   }
}
