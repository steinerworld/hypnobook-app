package net.steinerworld.hypnobook.ui.views.dashboard;

import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Übersicht")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends Div {
   private static final Logger LOGGER = LoggerFactory.getLogger(DashboardView.class);

   public DashboardView() {
      LOGGER.info(">>>> CHART");
      setSizeFull();

      VerticalBarChartExample example = new VerticalBarChartExample();
      add(example.build());
   }
}
