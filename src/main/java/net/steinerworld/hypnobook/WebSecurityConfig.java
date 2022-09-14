package net.steinerworld.hypnobook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import net.steinerworld.hypnobook.ui.views.login.LoginView;


@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends VaadinWebSecurity {
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      // Delegating the responsibility of general configurations
      // of http security to the super class. It is configuring
      // the followings: Vaadin's CSRF protection by ignoring
      // framework's internal requests, default request cache,
      // ignoring public views annotated with @AnonymousAllowed,
      // restricting access to other views/endpoints, and enabling
      // ViewAccessChecker authorization.
      // You can add any possible extra configurations of your own
      // here (the following is just an example):

      // http.rememberMe().alwaysRemember(false);

      super.configure(http);

      // This is important to register your login view to the
      // view access checker mechanism:
      setLoginView(http, LoginView.class);
   }

   /**
    * Allows access to static resources, bypassing Spring security.
    */
   @Override
   public void configure(WebSecurity web) throws Exception {
      // Configure your static resources with public access here:
      web.ignoring().antMatchers(
            "/images/**"
      );

      // Delegating the ignoring configuration for Vaadin's
      // related static resources to the super class:
      super.configure(web);
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
