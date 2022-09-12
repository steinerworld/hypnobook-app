package net.steinerworld.hypnobook.views.buchhaltung;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import net.steinerworld.hypnobook.views.MainLayout;

@PageTitle("Buchhaltung")
@Route(value = "buchhaltung", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class BuchhaltungView extends Div {
}
