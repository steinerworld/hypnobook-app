package net.steinerworld.hypnobook.services;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.AppUser;
import net.steinerworld.hypnobook.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class SecurityService {

   private static final String LOGOUT_SUCCESS_URL = "/";
   private final AppUserRepository appUserRepository;

   public Optional<AppUser> authenticatedAppUser() {
      Optional<UserDetails> maybe = evaluateUserDetails();
      if (maybe.isPresent()) {
         return appUserRepository.findByUsername(maybe.get().getUsername());
      }
      return Optional.empty();
   }

   private Optional<UserDetails> evaluateUserDetails() {
      SecurityContext context = SecurityContextHolder.getContext();
      Object principal = context.getAuthentication().getPrincipal();
      if (principal instanceof UserDetails) {
         return Optional.of((UserDetails) principal);
      }
      // Anonymous or no authentication.
      return Optional.empty();
   }

   public void logout() {
      UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
      SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
      logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
   }
}