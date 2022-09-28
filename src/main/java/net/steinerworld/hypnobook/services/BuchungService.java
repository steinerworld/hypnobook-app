package net.steinerworld.hypnobook.services;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Buchung;
import net.steinerworld.hypnobook.domain.Kategorie;
import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.repository.BuchungRepository;

@Service
@RequiredArgsConstructor
public class BuchungService {
   private final BuchungRepository buchungRepo;

   public List<Buchung> findAllSortedInPeriode(Steuerperiode periode) {
      return buchungRepo.findBySteuerperiode(periode, Sort.by(Sort.Direction.DESC, "buchungsdatum", "id"));
   }

   public double sumAusgabenInPeriode(Steuerperiode periode) {
      return findAllSortedInPeriode(periode).stream()
            .filter(buchung -> Objects.nonNull(buchung.getAusgabe()))
            .mapToDouble(Buchung::getAusgabe)
            .sum();
   }

   public double sumEinnahmenInPeriode(Steuerperiode periode) {
      return findAllSortedInPeriode(periode).stream()
            .filter(buchung -> Objects.nonNull(buchung.getEinnahme()))
            .mapToDouble(Buchung::getEinnahme)
            .sum();
   }

   public double sumAusgabeInPeriodeAndKategorie(Steuerperiode periode, Kategorie kat) {
      return buchungRepo.findBySteuerperiodeAndAndKategorie(periode, kat).stream()
            .filter(buchung -> Objects.nonNull(buchung.getAusgabe()))
            .mapToDouble(Buchung::getAusgabe)
            .sum();
   }

   public void save(Buchung buchung) {
      buchungRepo.save(buchung);
   }
}
