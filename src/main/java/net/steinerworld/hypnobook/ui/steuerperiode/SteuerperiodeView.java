package net.steinerworld.hypnobook.ui.steuerperiode;

import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.domain.SteuerperiodeState;
import net.steinerworld.hypnobook.repository.SteuerperiodeRepository;
import net.steinerworld.hypnobook.services.DateConverter;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Steuerperiode")
@Route(value = "steuerperiode", layout = MainLayout.class)
@RequiredArgsConstructor
public class SteuerperiodeView extends HorizontalLayout {

   private final SteuerperiodeRepository periodeRepo;
   private final DateConverter dateConverter;

   private String jahresbezeichnung;
   private LocalDate von;
   private LocalDate bis;
   private SteuerperiodeState status;


   @PostConstruct
   public void initialize() {
      ListBox<Steuerperiode> periodeListBox = new ListBox<>();
      periodeListBox.setRenderer(new ComponentRenderer<>(this::buildRenderer));
      periodeListBox.setItems(periodeRepo.findAll());

      add(periodeListBox);
   }

   private Component buildRenderer(Steuerperiode periode) {
      Span name = new Span(periode.getJahresbezeichnung());
      Span range = new Span(dateConverter.localDateToString(periode.getVon()) + " - " + dateConverter.localDateToString(periode.getBis()));
      Span badge = createFormattedBadge(periode.getStatus());

      VerticalLayout column = new VerticalLayout(name, range, badge);
      column.setPadding(false);
      column.setSpacing(false);

      Div div = new Div(column);
      div.addClassName("periode-card");
      return div;
   }

   private Span createFormattedBadge(SteuerperiodeState state) {
      Span badge = new Span(state.name());
      if (state == SteuerperiodeState.GESCHLOSSEN) {
         badge.getElement().getThemeList().add("badge error");
      } else if (state == SteuerperiodeState.AKTIV) {
         badge.getElement().getThemeList().add("badge success");
      } else {
         badge.getElement().getThemeList().add("badge contrast");
      }
      return badge;
   }
}
