package net.steinerworld.hypnobook.ui.views.taxperiod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;
import net.steinerworld.hypnobook.services.DateConverter;
import net.steinerworld.hypnobook.services.TaxPeriodService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - TaxPeriod")
@Route(value = "taxPeriod/:action?/:pid?", layout = MainLayout.class)
@RequiredArgsConstructor
public class TaxPeriodView extends HorizontalLayout implements BeforeEnterObserver {
   private static final Logger LOGGER = LoggerFactory.getLogger(TaxPeriodView.class);
   private static final String PARAMETER_PID = "pid";
   private static final String PARAMETER_ACTION = "action";
   private static final String PFLICHTFELD_TXT = "Pflichtfeld";
   private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "taxPeriod/edit/%s";
   private final String STEUERPERIODE_CREATE = "taxPeriod/create";

   private final DateConverter dateConverter;
   private final TaxPeriodService taxService;

   private ListBox<TaxPeriod> periodeListBox;
   private TaxPeriod taxPeriod;
   private IntegerField geschaeftsjahr;
   private DatePicker von;
   private DatePicker bis;
   private Select<TaxPeriodState> status;
   private BeanValidationBinder<TaxPeriod> binder;

   private static TaxPeriod createNew() {
      return new TaxPeriod().setStatus(TaxPeriodState.ERSTELLT);
   }

   @PostConstruct
   public void initialize() {
      Component listBox = createListBox();
      Component editForm = createEditForm();
      add(listBox, editForm);
   }

   private Component createListBox() {
      periodeListBox = new ListBox<>();
      periodeListBox.setRenderer(new ComponentRenderer<>(this::buildListRenderer));
      periodeListBox.setItems(taxService.findAll());
      // when a row is selected or deselected, populate form
      periodeListBox.addValueChangeListener(event -> {
         if (event.getValue() != null) {
            UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
         } else {
            UI.getCurrent().navigate(TaxPeriodView.class);
         }
      });

      Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
      plusButton.addClassName("periode-plus-button");
      plusButton.addClickListener(event -> UI.getCurrent().navigate(STEUERPERIODE_CREATE));
      return new Div(periodeListBox, plusButton);
   }

   private Span createFormattedBadge(TaxPeriodState state) {
      Span badge = new Span(state.name());
      if (state == TaxPeriodState.GESCHLOSSEN) {
         badge.getElement().getThemeList().add("badge error");
      } else if (state == TaxPeriodState.AKTIV) {
         badge.getElement().getThemeList().add("badge success");
      } else {
         badge.getElement().getThemeList().add("badge contrast");
      }
      return badge;
   }

   private Component buildListRenderer(TaxPeriod periode) {
      Span name = new Span(String.valueOf(periode.getGeschaeftsjahr()));
      Span range = new Span(dateConverter.localDateToString(periode.getVon()) + " - " + dateConverter.localDateToString(periode.getBis()));
      Span badge = createFormattedBadge(periode.getStatus());

      VerticalLayout column = new VerticalLayout(name, range, badge);
      column.setPadding(false);
      column.setSpacing(false);

      Div div = new Div(column);
      div.addClassName("periode-card");
      return div;
   }

   private Component createEditForm() {
      binder = new BeanValidationBinder<>(TaxPeriod.class);
      FormLayout formLayout = new FormLayout();
      geschaeftsjahr = new IntegerField("Geschäftsjahr");
      binder.forField(geschaeftsjahr).asRequired(PFLICHTFELD_TXT).bind(TaxPeriod::getGeschaeftsjahr, TaxPeriod::setGeschaeftsjahr);
      von = new DatePicker("von");
      binder.forField(von).asRequired(PFLICHTFELD_TXT).bind(TaxPeriod::getVon, TaxPeriod::setVon);
      bis = new DatePicker("bis");
      binder.forField(bis).asRequired(PFLICHTFELD_TXT).bind(TaxPeriod::getBis, TaxPeriod::setBis);
      status = new Select<>();
      status.setLabel("Status");
      status.setItems(TaxPeriodState.values());
      status.setEnabled(false);
      binder.forField(status).bind(TaxPeriod::getStatus, TaxPeriod::setStatus);
      formLayout.add(geschaeftsjahr, von, bis, status);

      HorizontalLayout buttons = createButtonLayout();
      VerticalLayout panel = new VerticalLayout(formLayout, buttons);
      panel.setVisible(false);
      binder.addStatusChangeListener(event -> panel.setVisible(Objects.nonNull(event.getBinder().getBean())));
      return panel;
   }

   private HorizontalLayout createButtonLayout() {
      HorizontalLayout buttonLayout = new HorizontalLayout();

      Button cancel = new Button("Cancel", e -> {
         binder.setBean(null);
         periodeListBox.setValue(null);
      });
      cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
      Button save = new Button("Save", e -> {
         BinderValidationStatus<TaxPeriod> validate = binder.validate();
         if (validate.isOk()) {
            TaxPeriod bean = binder.getBean();
            taxService.save(bean);
            periodeListBox.setItems(taxService.findAll());
            binder.setBean(null);
         } else {
            Notification.show("Keine valide Steuerperiode");
         }
      });
      save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      Button changeStatus = new Button("Aktivieren", e -> confirmChangeActiveTaxperiode(taxPeriod));
      changeStatus.addThemeVariants(ButtonVariant.LUMO_ERROR);
      Anchor balancePdf = buildAnchorForTaxSheet();
      binder.addStatusChangeListener(e -> {
         Optional<TaxPeriod> maybeSP = Optional.ofNullable((TaxPeriod) e.getBinder().getBean());
         if (maybeSP.isPresent()) {
            changeStatus.setVisible(maybeSP.get().getStatus() == TaxPeriodState.ERSTELLT);
            balancePdf.setVisible(maybeSP.get().getStatus() == TaxPeriodState.GESCHLOSSEN);
         } else {
            changeStatus.setVisible(false);
            balancePdf.setVisible(false);
         }
      });

      buttonLayout.add(save, cancel, changeStatus, balancePdf);
      return buttonLayout;
   }

   private Anchor buildAnchorForTaxSheet() {
      Anchor anchor = new Anchor(new StreamResource("Jahresabschluss.pdf", (InputStreamFactory) () -> {
         ByteArrayOutputStream os = taxService.streamBalanceSheet(taxPeriod);
         return new ByteArrayInputStream(os.toByteArray());
      }), "");
      anchor.getElement().setAttribute("download", true);
      anchor.add(new Button("Download Jahresabschluss"));
      return anchor;
   }

   private void confirmChangeActiveTaxperiode(TaxPeriod periode) {
      Optional<TaxPeriod> maybeActive = taxService.findActive();
      if (maybeActive.isPresent()) {
         TaxPeriod active = maybeActive.get();
         ConfirmDialog dialog = new ConfirmDialog();
         dialog.setHeader("Konsequenzen der Aktivierung ver Steuerperiode " + periode.getGeschaeftsjahr());
         dialog.setText("Du schliesst somit die aktuell aktive Steuerperiode '" + active.getGeschaeftsjahr()
               + "'\nDas kann nicht mehr rückgängig gemacht werden!");

         dialog.setCancelable(true);
         dialog.setConfirmText("Aktivieren");
         dialog.setConfirmButtonTheme("error primary");
         dialog.addConfirmListener(e -> {
            taxService.changeActiveTaxPeriod(active, periode);
            periodeListBox.setItems(taxService.findAll());
         });
         dialog.open();
      } else {
         LOGGER.info("Keine aktive Steuerperiode gefunden -> einfach machen");
      }
   }

   @Override public void beforeEnter(BeforeEnterEvent event) {
      Optional<String> maybeAction = event.getRouteParameters().get(PARAMETER_ACTION);
      maybeAction.ifPresent(action -> {
         if (action.equals("create")) {
            populateForm(createNew());
         } else if (action.equals("edit")) {
            Optional<String> maybePid = event.getRouteParameters().get(PARAMETER_PID);
            maybePid.ifPresent(pid -> {
               UUID periodeId = UUID.fromString(pid);
               Optional<TaxPeriod> maybePeriodeFromBackend = taxService.get(periodeId);
               if (maybePeriodeFromBackend.isPresent()) {
                  populateForm(maybePeriodeFromBackend.get());
               } else {
                  Notification.show(String.format("The requested TaxPeriod was not found, ID = %s", pid), 3000,
                        Notification.Position.BOTTOM_START);
                  event.forwardTo(TaxPeriodView.class);
               }
            });
         }
      });
   }

   private void populateForm(TaxPeriod value) {
      this.taxPeriod = value;
      binder.setBean(this.taxPeriod);
   }

}
