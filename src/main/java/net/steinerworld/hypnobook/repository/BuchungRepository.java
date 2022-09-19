package net.steinerworld.hypnobook.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.steinerworld.hypnobook.domain.Buchung;
import net.steinerworld.hypnobook.domain.Steuerperiode;

/**
 * Spring Data JPA repository for the Buchung entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuchungRepository extends JpaRepository<Buchung, Long> {
   List<Buchung> findBySteuerperiode(Steuerperiode periode, Sort sort);
}
