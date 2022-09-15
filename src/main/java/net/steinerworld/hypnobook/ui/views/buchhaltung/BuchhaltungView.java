package net.steinerworld.hypnobook.ui.views.buchhaltung;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Buchhaltung;
import net.steinerworld.hypnobook.repository.BuchhaltungRepository;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Buchhaltung")
@Route(value = "buchhaltung", layout = MainLayout.class)
@RequiredArgsConstructor
public class BuchhaltungView extends VerticalLayout {
   private final BuchhaltungRepository buchhaltungRepository;

   private Grid<Buchhaltung> grid;

   @PostConstruct
   public void initialize() {
      layoutGrid();
      grid.setItems(buchhaltungRepository.findAll(Sort.by(Sort.Direction.DESC, "Buchungsdatum")));
      add(grid);
   }

   private void layoutGrid() {
      grid = new Grid<>();
      grid.addColumn(Buchhaltung::getBuchungsdatum).setHeader("Datum");
      grid.addColumn(Buchhaltung::getEinnahme).setHeader("Einnahme");
      grid.addColumn(Buchhaltung::getAusgabe).setHeader("Ausgabe");
      grid.addColumn(Buchhaltung::getBelegNr).setHeader("Beleg-Nr.");
      grid.addColumn(Buchhaltung::getText).setHeader("Buchungstext");
   }
}
