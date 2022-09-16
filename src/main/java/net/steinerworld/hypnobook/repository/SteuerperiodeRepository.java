package net.steinerworld.hypnobook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.domain.SteuerperiodeState;

/**
 * Spring Data JPA repository for the Steuerperiode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SteuerperiodeRepository extends JpaRepository<Steuerperiode, Long> {
   Steuerperiode findByStatusEquals(SteuerperiodeState status);
}
