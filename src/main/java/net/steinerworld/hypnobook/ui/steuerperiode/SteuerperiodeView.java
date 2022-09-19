package net.steinerworld.hypnobook.ui.steuerperiode;

import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

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
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Steuerperiode")
@Route(value = "steuerperiode", layout = MainLayout.class)
@RequiredArgsConstructor
public class SteuerperiodeView extends HorizontalLayout {

   private final SteuerperiodeRepository periodeRepo;

   private String jahresbezeichnung;
   private LocalDate von;
   private LocalDate bis;
   private SteuerperiodeState status;


   @PostConstruct
   public void initialize() {
      ListBox<Steuerperiode> periodeListBox = new ListBox<>();
      periodeListBox.setRenderer(new ComponentRenderer<>(periode -> {
         Span name = new Span(periode.getJahresbezeichnung());
         Span range = new Span(periode.getVon() + " - " + periode.getBis());
         range.getStyle()
               .set("color", "var(--lumo-secondary-text-color)")
               .set("font-size", "var(--lumo-font-size-s)");

         VerticalLayout column = new VerticalLayout(name, range);
         column.setPadding(false);
         column.setSpacing(false);

         return column;
      }));
      periodeListBox.setItems(periodeRepo.findAll());

      add(periodeListBox);
   }
}
