package net.steinerworld.hypnobook.ui.views.dashboard;

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

      addBarChart();
      addPieChart();
   }

   private void addBarChart() {
      VerticalBarChartExample example = new VerticalBarChartExample();
      Div div = new Div(example.build());
      div.setWidth("50%");
      add(div);
   }

   private void addPieChart() {
      taxService.findActive().ifPresent(tax -> {
         LOGGER.info("create CatPie for {}", tax);
         Map<String, Double> realSums = taxService.sumByTaxAndCats(tax).entrySet().stream()
               .filter(entry -> entry.getValue() > 0)
               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

         PieChartExample example = new PieChartExample(realSums);
         Div div = new Div(example.build());
         div.setWidth("50%");
         add(div);
      });

   }
}
