package net.steinerworld.hypnobook.ui.views.profile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import elemental.json.Json;
import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.AppUser;
import net.steinerworld.hypnobook.domain.AppUserRole;
import net.steinerworld.hypnobook.exceptions.MaloneyException;
import net.steinerworld.hypnobook.services.AppUserService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Profil")
@Route(value = "profile", layout = MainLayout.class)
@RequiredArgsConstructor
public class ProfileView extends HorizontalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(ProfileView.class);

   private final AppUserService appUserService;
   private final Binder<AppUser> userBinder = new Binder<>(AppUser.class);

   @PostConstruct
   public void initialize() {
      addUserListBox();
      addAlterEgo();
      addUserForm();
      userBinder.addStatusChangeListener(event -> LOGGER.info("AppUser: {}", userBinder.getBean()));
   }

   private void addUserListBox() {
      ListBox<AppUser> userListBox = new ListBox<>();
      userListBox.setHeightFull();
      userListBox.setRenderer(new ComponentRenderer<>(this::buildListRenderer));
      userListBox.setItems(appUserService.findAll());
      userListBox.addValueChangeListener(event -> userBinder.setBean(event.getValue()));

      Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
      plusButton.addClassName("periode-plus-button");
      plusButton.addClickListener(e -> Notification.show("Noch nicht implementiert"));
      add(new Div(userListBox, plusButton));
   }

   private Component buildListRenderer(AppUser user) {
      HorizontalLayout row = new HorizontalLayout();
      row.setAlignItems(FlexComponent.Alignment.CENTER);

      Avatar avatar = new Avatar();
      avatar.setName(user.getName());
      StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(user.getProfilePicture()));
      avatar.setImageResource(resource);

      Span name = new Span(user.getName());
      Span username = new Span(user.getUsername() + " (" + user.getRoles() + ")");
      username.getStyle()
            .set("color", "var(--lumo-secondary-text-color)")
            .set("font-size", "var(--lumo-font-size-s)");

      VerticalLayout column = new VerticalLayout(name, username);
      column.setPadding(false);
      column.setSpacing(false);

      row.add(avatar, column);
      row.getStyle().set("line-height", "var(--lumo-line-height-m)");
      return row;
   }

   private void addAlterEgo() {
      Image alterEgo = new Image("images/AnonymUser.png", "Anonymus");
      alterEgo.setAlt("Alter Ego");
      alterEgo.setWidth("200px");
      alterEgo.setHeight("200px");

      Upload upload = getUploadAreaAlterEgo();
      upload.setVisible(false);

      VerticalLayout layout = new VerticalLayout();
      layout.setAlignItems(Alignment.CENTER);
      layout.setMaxWidth("250px");
      layout.add(alterEgo, upload);
      add(layout);

      userBinder.addStatusChangeListener(e -> {
         AppUser user = userBinder.getBean();
         if (user != null) {
            StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(user.getProfilePicture()));
            alterEgo.setSrc(resource);
            upload.setVisible(true);
         }
      });
   }

   private Upload getUploadAreaAlterEgo() {
      MemoryBuffer memoryBuffer = new MemoryBuffer();
      Upload upload = new Upload(memoryBuffer);
      upload.setDropAllowed(false);
      Button uploadButton = new Button(new Icon(VaadinIcon.UPLOAD));
      upload.setUploadButton(uploadButton);

      upload.getElement()
            .addEventListener("max-files-reached-changed", event -> {
               boolean maxFilesReached = event.getEventData()
                     .getBoolean("event.detail.value");
               uploadButton.setEnabled(!maxFilesReached);
            }).addEventData("event.detail.value");

      upload.addSucceededListener(event -> {
         InputStream fileData = memoryBuffer.getInputStream();
         String fileName = event.getFileName();
         processProfilePicture(fileData, fileName);
         upload.getElement().setPropertyJson("files", Json.createArray());
      });
      return upload;
   }

   private void processProfilePicture(InputStream is, String fileName) {
      try {
         AppUser appUser = userBinder.getBean();
         if (appUser != null) {
            appUser.setProfilePicture(is.readAllBytes());
            AppUser savedUser = appUserService.save(appUser);
            userBinder.setBean(savedUser);
            Notification.show("Neues Profilbild gespeichert");
            LOGGER.info("new ProfilePicture {} saved", fileName);
         }
      } catch (IOException e) {
         throw new MaloneyException("can not save the new ProfilePicture", e);
      }
   }

   private void addUserForm() {
      TextField fNameTextField = new TextField("Name");
      TextField uNameTextField = new TextField("Benutzername");
      TextField pWortField = new TextField("Passwort");
      pWortField.setEnabled(false);

      Select<AppUserRole> roleSelect = new Select<>();
      roleSelect.setLabel("Rolle");
      roleSelect.setItemLabelGenerator(AppUserRole::name);
      roleSelect.setItems(AppUserRole.values());


      FormLayout layout = new FormLayout(fNameTextField, uNameTextField, pWortField, roleSelect);
      layout.setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("500px", 2));
      add(layout);

      userBinder.forField(fNameTextField)
            .bind(AppUser::getName, AppUser::setName);
      userBinder.forField(uNameTextField)
            .bind(AppUser::getUsername, AppUser::setUsername);
      userBinder.forField(roleSelect)
            .bind(AppUser::getRoles, AppUser::setRoles);
   }


}
