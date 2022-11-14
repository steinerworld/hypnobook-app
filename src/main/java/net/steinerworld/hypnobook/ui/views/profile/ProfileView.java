package net.steinerworld.hypnobook.ui.views.profile;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Profil")
@Route(value = "profile", layout = MainLayout.class)
@RequiredArgsConstructor
public class ProfileView extends FormLayout {

   @PostConstruct
   public void initialize() {
      add(new Label("Aber Hallo"));
   }
}
