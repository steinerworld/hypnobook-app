package net.steinerworld.hypnobook.ui.views.dashboard;

import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Übersicht")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(DashboardView.class);

   public DashboardView() {
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
      PieChartExample example = new PieChartExample();
      Div div = new Div(example.build());
      div.setWidth("50%");
      add(div);
   }
}
