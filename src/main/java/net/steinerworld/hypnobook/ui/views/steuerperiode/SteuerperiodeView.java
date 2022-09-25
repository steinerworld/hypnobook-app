package net.steinerworld.hypnobook.ui.views.steuerperiode;

import java.util.Objects;
import java.util.Optional;

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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.domain.SteuerperiodeState;
import net.steinerworld.hypnobook.services.DateConverter;
import net.steinerworld.hypnobook.services.SteuerperiodeService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Steuerperiode")
@Route(value = "steuerperiode/:action?/:pid?", layout = MainLayout.class)
@RequiredArgsConstructor
public class SteuerperiodeView extends HorizontalLayout implements BeforeEnterObserver {
   private static final Logger LOGGER = LoggerFactory.getLogger(SteuerperiodeView.class);
   private static final String PARAMETER_PID = "pid";
   private static final String PARAMETER_ACTION = "action";
   private static final String PFLICHTFELD_TXT = "Pflichtfeld";
   private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "steuerperiode/edit/%s";
   private final String STEUERPERIODE_CREATE = "steuerperiode/create";

   private final DateConverter dateConverter;
   private final SteuerperiodeService periodeService;

   private ListBox<Steuerperiode> periodeListBox;
   private Steuerperiode steuerperiode;
   private TextField jahresbezeichnung;
   private DatePicker von;
   private DatePicker bis;
   private Select<SteuerperiodeState> status;
   private BeanValidationBinder<Steuerperiode> binder;

   private static Steuerperiode createNew() {
      return new Steuerperiode().setStatus(SteuerperiodeState.ERSTELLT);
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
      periodeListBox.setItems(periodeService.findAll());
      // when a row is selected or deselected, populate form
      periodeListBox.addValueChangeListener(event -> {
         if (event.getValue() != null) {
            UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
         } else {
            //            clearForm();
            UI.getCurrent().navigate(SteuerperiodeView.class);
         }
      });

      Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
      plusButton.addClassName("periode-plus-button");
      plusButton.addClickListener(event -> UI.getCurrent().navigate(STEUERPERIODE_CREATE));
      return new Div(periodeListBox, plusButton);
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

   private Component buildListRenderer(Steuerperiode periode) {
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

   private Component createEditForm() {
      binder = new BeanValidationBinder<>(Steuerperiode.class);
      FormLayout formLayout = new FormLayout();
      jahresbezeichnung = new TextField("Jahresbezeichnung");
      binder.forField(jahresbezeichnung).asRequired(PFLICHTFELD_TXT).bind(Steuerperiode::getJahresbezeichnung, Steuerperiode::setJahresbezeichnung);
      von = new DatePicker("von");
      binder.forField(von).asRequired(PFLICHTFELD_TXT).bind(Steuerperiode::getVon, Steuerperiode::setVon);
      bis = new DatePicker("bis");
      binder.forField(bis).asRequired(PFLICHTFELD_TXT).bind(Steuerperiode::getBis, Steuerperiode::setBis);
      status = new Select<>();
      status.setLabel("Status");
      status.setItems(SteuerperiodeState.values());
      status.setEnabled(false);
      binder.forField(status).bind(Steuerperiode::getStatus, Steuerperiode::setStatus);
      formLayout.add(jahresbezeichnung, von, bis, status);

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
         BinderValidationStatus<Steuerperiode> validate = binder.validate();
         if (validate.isOk()) {
            Steuerperiode bean = binder.getBean();
            periodeService.save(bean);
            periodeListBox.setItems(periodeService.findAll());
            binder.setBean(null);
         } else {
            Notification.show("Keine valide Steuerperiode");
         }
      });
      save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      Button changeStatus = new Button("Aktivieren", e -> {
         activateSteuerperiode(steuerperiode);
      });
      changeStatus.addThemeVariants(ButtonVariant.LUMO_ERROR);
      binder.addStatusChangeListener(e -> {
         Optional<Steuerperiode> maybeSP = Optional.ofNullable((Steuerperiode) e.getBinder().getBean());
         if (maybeSP.isPresent()) {
            changeStatus.setVisible(maybeSP.get().getStatus() == SteuerperiodeState.ERSTELLT);
         } else {
            changeStatus.setVisible(false);
         }
      });

      buttonLayout.add(save, cancel, changeStatus);
      return buttonLayout;
   }

   private void activateSteuerperiode(Steuerperiode periode) {
      Optional<Steuerperiode> maybeActive = periodeService.findActive();
      if (maybeActive.isPresent()) {
         ConfirmDialog dialog = new ConfirmDialog();
         dialog.setHeader("Wirklich Aktivieren?");
         dialog.setText("Du schliesst somit die Steuerperiode 'BLA'\nDas kann nichtmehr rückgängig gemacht werden!");

         dialog.setCancelable(true);
         dialog.setConfirmText("Aktivieren");
         dialog.setConfirmButtonTheme("error primary");
         dialog.addConfirmListener(event -> LOGGER.info(">>>>>> Aktivieren"));
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
               long periodeId = Long.parseLong(pid);
               Optional<Steuerperiode> maybePeriodeFromBackend = periodeService.get(periodeId);
               if (maybePeriodeFromBackend.isPresent()) {
                  populateForm(maybePeriodeFromBackend.get());
               } else {
                  Notification.show(String.format("The requested Steuerperiode was not found, ID = %s", pid), 3000,
                        Notification.Position.BOTTOM_START);
                  event.forwardTo(SteuerperiodeView.class);
               }
            });
         }
      });
   }

   private void populateForm(Steuerperiode value) {
      this.steuerperiode = value;
      binder.setBean(this.steuerperiode);
   }

}
