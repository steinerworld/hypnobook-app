package net.steinerworld.hypnobook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.steinerworld.hypnobook.domain.Buchung;

/**
 * Spring Data JPA repository for the Buchung entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuchungRepository extends JpaRepository<Buchung, Long> {
}
