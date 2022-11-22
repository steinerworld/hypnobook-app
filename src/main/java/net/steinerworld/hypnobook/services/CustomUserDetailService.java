package net.steinerworld.hypnobook.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.AppUser;
import net.steinerworld.hypnobook.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
   private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailService.class);
   private final AppUserRepository userRepository;

   @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      LOGGER.info("User '{}' will rein", username);
      return userRepository.findByUsername(username)
            .map(CustomUserDetailService::createUserDetailsFrom)
            .orElseThrow(() -> new UsernameNotFoundException("Benutzer nicht gefunden"));
   }

   private static UserDetails createUserDetailsFrom(AppUser usr) {
      return User.withUsername(usr.getUsername())
            .password(usr.getPassword())
            .roles(usr.getRoles().name())
            .build();
   }
}
