package net.steinerworld.hypnobook.ui.views.dashboard;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Übersicht")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

   public DashboardView() {
      setHeightFull();
      setWidthFull();

      Label title = new Label("1. Quartal");
      title.setClassName("ov-title");

      Label subtitle = new Label("Jan. - Mrz.");
      subtitle.setClassName("ov-subtitle");

      Label inMoney = new Label("3'256.35");
      inMoney.setClassName("ov-in-money");

      Label outMoney = new Label("495.00");
      outMoney.setClassName("ov-out-money");

      Div div = new Div(title, subtitle, inMoney, outMoney);
      div.setClassName("ov-card");
      add(div);
   }
}
