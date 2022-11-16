package net.steinerworld.hypnobook.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.AppUser;
import net.steinerworld.hypnobook.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class AppUserService {
   private final AppUserRepository appUserRepository;

   public List<AppUser> findAll() {
      return appUserRepository.findAll();
   }
}
