package net.steinerworld.hypnobook.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.domain.TaxPeriod;

/**
 * Spring Data JPA repository for the Accounting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountingRepository extends JpaRepository<Accounting, Long> {
   List<Accounting> findBySteuerperiode(TaxPeriod periode, Sort sort);

   List<Accounting> findBySteuerperiodeAndAndKategorie(TaxPeriod periode, Category category);

   long countAllBySteuerperiode(TaxPeriod periode);
}
