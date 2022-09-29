package net.steinerworld.hypnobook.services;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.repository.AccountingRepository;

@Service
@RequiredArgsConstructor
public class AccountingService {
   private final AccountingRepository buchungRepo;

   public List<Accounting> findAllSortedInPeriode(TaxPeriod periode) {
      return buchungRepo.findByTaxPeriod(periode, Sort.by(Sort.Direction.DESC, "buchungsdatum", "id"));
   }

   public double sumAusgabenInPeriode(TaxPeriod periode) {
      return findAllSortedInPeriode(periode).stream()
            .filter(accounting -> Objects.nonNull(accounting.getAusgabe()))
            .mapToDouble(Accounting::getAusgabe)
            .sum();
   }

   public double sumEinnahmenInPeriode(TaxPeriod periode) {
      return findAllSortedInPeriode(periode).stream()
            .filter(accounting -> Objects.nonNull(accounting.getEinnahme()))
            .mapToDouble(Accounting::getEinnahme)
            .sum();
   }

   public double sumAusgabeInPeriodeAndKategorie(TaxPeriod periode, Category kat) {
      return buchungRepo.findByTaxPeriodAndCategory(periode, kat).stream()
            .filter(accounting -> Objects.nonNull(accounting.getAusgabe()))
            .mapToDouble(Accounting::getAusgabe)
            .sum();
   }

   public void save(Accounting accounting) {
      buchungRepo.save(accounting);
   }
}
