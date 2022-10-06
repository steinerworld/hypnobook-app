package net.steinerworld.hypnobook.ui.views.dashboard;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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

      Span title = new Span("1. Quartal 2022");
      title.setClassName("ov-title");

      Span subtitle = new Span("Jan. - Mrz.");
      subtitle.setClassName("ov-subtitle");

      Span inMoney = new Span("3'256.35");
      inMoney.setClassName("ov-in-money");

      Span outMoney = new Span("495.00");
      outMoney.setClassName("ov-out-money");

      Icon upIcon = new Icon(VaadinIcon.ARROW_UP);

      //      VerticalLayout vl = new VerticalLayout(title, subtitle, inMoney, outMoney);
      //      HorizontalLayout hl = new HorizontalLayout(vl, upIcon);

      Div div = new Div(title, subtitle, inMoney, outMoney, upIcon);
      div.setClassName("ov-card");
      add(div);
   }
}
