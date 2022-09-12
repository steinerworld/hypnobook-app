package net.steinerworld.hypnobook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.steinerworld.hypnobook.domain.Kategorie;

/**
 * Spring Data JPA repository for the Kategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KategorieRepository extends JpaRepository<Kategorie, Long> {
}
