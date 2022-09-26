package net.steinerworld.hypnobook.ui.views.kategorie;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Kategorie")
@Route(value = "kategorie", layout = MainLayout.class)
@RequiredArgsConstructor
public class KategorieView extends VerticalLayout {

   @PostConstruct
   public void initialize() {
      Text text = new Text("Aber Hallo");
      add(text);
   }
}
