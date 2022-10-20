package net.steinerworld.hypnobook.ui.views.login;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.security.SecurityService;

@PageTitle("Login")
@Route(value = "login")
@RequiredArgsConstructor
public class LoginView extends LoginOverlay implements BeforeEnterObserver {
   private final SecurityService securityService;

   @PostConstruct
   public void initialize() {
      setAction("login");

      LoginI18n i18n = LoginI18n.createDefault();
      i18n.setHeader(new LoginI18n.Header());
      i18n.getHeader().setTitle("Hypno Book");
      i18n.getHeader().setDescription("Die einfache Buchhaltung");
      i18n.setAdditionalInformation(null);
      setI18n(i18n);

      setForgotPasswordButtonVisible(false);
      setOpened(true);
   }

   @Override
   public void beforeEnter(BeforeEnterEvent event) {
      if (securityService.authenticatedAppUser().isPresent()) {
         // Already logged in
         setOpened(false);
         event.forwardTo("");
      }
      setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
   }
}