package net.steinerworld.hypnobook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.steinerworld.hypnobook.domain.Buchhaltung;

/**
 * Spring Data JPA repository for the Buchhaltung entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuchhaltungRepository extends JpaRepository<Buchhaltung, Long> {
}
