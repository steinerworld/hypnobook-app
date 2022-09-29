package net.steinerworld.hypnobook.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;

/**
 * Spring Data JPA repository for the TaxPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxPeriodRepository extends JpaRepository<TaxPeriod, UUID> {
   TaxPeriod findByStatusEquals(TaxPeriodState status);
}
