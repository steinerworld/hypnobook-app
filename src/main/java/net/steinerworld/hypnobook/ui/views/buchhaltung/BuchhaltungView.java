package net.steinerworld.hypnobook.ui.views.buchhaltung;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Buchhaltung;
import net.steinerworld.hypnobook.repository.BuchhaltungRepository;
import net.steinerworld.hypnobook.ui.components.buchhaltung.AusgabeForm;
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

      Dialog ausgabeDialog = createAusgabeDialog();
      Button button = new Button("Neue Ausgabe erfassen", e -> ausgabeDialog.open());

      add(grid, ausgabeDialog, button);
   }

   private static Dialog createAusgabeDialog() {
      Dialog ausgabeDialog = new Dialog();
      ausgabeDialog.setModal(true);
      ausgabeDialog.setHeaderTitle("Neue Ausgabe erfassen");

      ausgabeDialog.add(new AusgabeForm());

      Button saveButton = createSaveButton(ausgabeDialog);
      Button cancelButton = new Button("Cancel", e -> ausgabeDialog.close());
      ausgabeDialog.getFooter().add(cancelButton);
      ausgabeDialog.getFooter().add(saveButton);
      return ausgabeDialog;
   }

   private void layoutGrid() {
      grid = new Grid<>();
      grid.addColumn(Buchhaltung::getBuchungsdatum).setHeader("Datum");
      grid.addColumn(Buchhaltung::getEinnahme).setHeader("Einnahme");
      grid.addColumn(Buchhaltung::getAusgabe).setHeader("Ausgabe");
      grid.addColumn(Buchhaltung::getBelegNr).setHeader("Beleg-Nr.");
      grid.addColumn(Buchhaltung::getText).setHeader("Buchungstext");
   }

   private static Button createSaveButton(Dialog dialog) {
      Button saveButton = new Button("Speichern", e -> dialog.close());
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      return saveButton;
   }
}
