package net.steinerworld.hypnobook.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Buchung;
import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.repository.BuchungRepository;

@Service
@RequiredArgsConstructor
public class BuchungService {
   private final BuchungRepository buchungRepo;

   public List<Buchung> findAllSortedInPeriode(Steuerperiode periode) {
      return buchungRepo.findBySteuerperiode(periode, Sort.by(Sort.Direction.DESC, "buchungsdatum", "id"));
   }

   public void save(Buchung buchung) {
      buchungRepo.save(buchung);
   }
}
