package net.steinerworld.hypnobook.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import net.steinerworld.hypnobook.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
   Optional<AppUser> findByUsername(String username);
}