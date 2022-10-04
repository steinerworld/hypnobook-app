package net.steinerworld.hypnobook.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.domain.TaxPeriod;

/**
 * Spring Data JPA repository for the Accounting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountingRepository extends JpaRepository<Accounting, UUID> {
   List<Accounting> findByTaxPeriod(TaxPeriod periode, Sort sort);

   List<Accounting> findByTaxPeriodAndCategory(TaxPeriod periode, Category category);

   long countAllByTaxPeriod(TaxPeriod periode);

   @Query(value = "SELECT nextval('booking_sequence')", nativeQuery = true)
   Long getNextBelegNr();
}
