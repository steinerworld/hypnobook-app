package net.steinerworld.hypnobook.ui.views.buchung;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Buchung;
import net.steinerworld.hypnobook.domain.BuchungType;
import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.domain.SteuerperiodeState;
import net.steinerworld.hypnobook.exceptions.MaloneyException;
import net.steinerworld.hypnobook.repository.BuchungRepository;
import net.steinerworld.hypnobook.repository.KategorieRepository;
import net.steinerworld.hypnobook.repository.SteuerperiodeRepository;
import net.steinerworld.hypnobook.ui.components.buchung.BuchungEditForm;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Buchung")
@Route(value = "buchung", layout = MainLayout.class)
@RequiredArgsConstructor
public class BuchungView extends VerticalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(BuchungView.class);
   private final BuchungRepository buchungRepository;
   private final KategorieRepository kategorieRepository;
   private final SteuerperiodeRepository periodeRepository;
   private final BeanValidationBinder<Buchung> buchungBinder = new BeanValidationBinder<>(Buchung.class);
   private final Grid<Buchung> grid = new Grid<>();

   private Dialog ausgabeDialog;
   private Dialog einnahmeDialog;

   @PostConstruct
   public void initialize() {
      setHeightFull();

      add(createBuchhaltungGrid());

      BuchungEditForm ausgabeForm = BuchungEditForm.builder().kategorieList(kategorieRepository::findAll)
            .periodeList(periodeRepository::findAll)
            .binder(buchungBinder)
            .type(BuchungType.AUSGABE)
            .build().get();
      ausgabeDialog = createBuchungDialog("Ausgabe", ausgabeForm);
      Button ausgabeButton = buildOpenDialogButton(BuchungType.AUSGABE, "Neue Ausgabe erfassen", ausgabeDialog);

      BuchungEditForm einnahmeForm = BuchungEditForm.builder().kategorieList(kategorieRepository::findAll)
            .periodeList(periodeRepository::findAll)
            .binder(buchungBinder)
            .type(BuchungType.EINNAHME)
            .build().get();
      einnahmeDialog = createBuchungDialog("Einnahme", einnahmeForm);
      Button einnahmeButton = buildOpenDialogButton(BuchungType.EINNAHME, "Neue Einnahme erfassen", einnahmeDialog);

      HorizontalLayout dialogButtons = new HorizontalLayout(ausgabeButton, einnahmeButton);
      add(ausgabeDialog, einnahmeDialog, dialogButtons);
   }

   private Grid<Buchung> createBuchhaltungGrid() {
      grid.addColumn(Buchung::getBuchungsdatum).setHeader("Datum");
      grid.addColumn(Buchung::getEinnahme).setHeader("Einnahme");
      grid.addColumn(Buchung::getAusgabe).setHeader("Ausgabe");
      grid.addColumn(Buchung::getBelegNr).setHeader("Beleg-Nr.");
      grid.addColumn(Buchung::getText).setHeader("Buchungstext");
      grid.setHeightFull();
      grid.addItemDoubleClickListener(event -> {
         Buchung item = event.getItem();
         buchungBinder.setBean(item);
         if (item.getBuchungType() == BuchungType.AUSGABE) {
            ausgabeDialog.open();
         } else if (item.getBuchungType() == BuchungType.EINNAHME) {
            einnahmeDialog.open();
         } else {
            throw new MaloneyException("Weder Ausgabe noch Eingabe Typ");
         }
      });
      loadGridData();
      return grid;
   }

   private Button buildOpenDialogButton(BuchungType type, String text, Dialog dialog) {
      return new Button(text, e -> {
         buchungBinder.setBean(newBuchhaltung(type));
         dialog.open();
      });
   }

   private void loadGridData() {
      grid.setItems(buchungRepository.findAll(Sort.by(Sort.Direction.DESC, "buchungsdatum", "id")));
   }

   private Dialog createBuchungDialog(String title, FormLayout form) {
      Dialog dialog = new Dialog();
      dialog.setModal(true);
      dialog.setHeaderTitle(title);
      dialog.add(form);
      Button saveButton = new Button("Buchen", e -> {
         BinderValidationStatus<Buchung> validate = buchungBinder.validate();
         if (validate.isOk()) {
            Buchung bean = buchungBinder.getBean();
            bean.setSteuerperiode(getPeriodeFromNew(bean));
            saveBuchungAndRefresh(bean);
            dialog.close();
         } else {
            Notification.show("Keine valide Buchung");
         }
      });
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      Button cancelButton = new Button("Cancel", e -> dialog.close());
      dialog.getFooter().add(cancelButton);
      dialog.getFooter().add(saveButton);
      return dialog;
   }

   private Steuerperiode getPeriodeFromNew(Buchung item) {
      return periodeRepository.findAll().stream()
            .filter(periode -> item.getBuchungsdatum().isAfter(periode.getVon()))
            .filter(periode -> item.getBuchungsdatum().isBefore(periode.getBis()))
            .findFirst()
            .orElseThrow();

   }

   private void saveBuchungAndRefresh(Buchung entity) {
      buchungRepository.save(entity);
      loadGridData();
   }

   private Buchung newBuchhaltung(BuchungType type) {
      return new Buchung()
            .setBuchungType(type)
            .setSteuerperiode(periodeRepository.findByStatusEquals(SteuerperiodeState.AKTIV))
            .setBuchungsdatum(LocalDate.now(ZoneId.systemDefault()));
   }

}
