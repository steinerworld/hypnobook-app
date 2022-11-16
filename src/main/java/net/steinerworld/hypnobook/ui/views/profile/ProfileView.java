package net.steinerworld.hypnobook.ui.views.profile;

import java.io.ByteArrayInputStream;

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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.AppUser;
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
      addUserForm();
      userBinder.addStatusChangeListener(event -> LOGGER.info("AppUser: {}", userBinder.getBean()));
   }

   private void addUserListBox() {
      ListBox<AppUser> userListBox = new ListBox<>();
      userListBox.setRenderer(new ComponentRenderer<>(this::buildListRenderer));
      userListBox.setItems(appUserService.findAll());
      userListBox.addValueChangeListener(event -> userBinder.setBean(event.getValue()));

      Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
      plusButton.addClassName("periode-plus-button");
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

   private void addUserForm() {
      TextField nameTextField = new TextField("Name");
      TextField usernameTextField = new TextField("Benutzername");
      TextField rolesTextField = new TextField("Rollen");


      FormLayout layout = new FormLayout(nameTextField, usernameTextField, rolesTextField);
      layout.setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("500px", 2));
      add(layout);
   }


}
