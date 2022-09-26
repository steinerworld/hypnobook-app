package net.steinerworld.hypnobook.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Kategorie;
import net.steinerworld.hypnobook.repository.KategorieRepository;

@Service
@RequiredArgsConstructor
public class KategorieService {
   private final KategorieRepository kategorieRepo;

   public List<Kategorie> findAll() {
      return kategorieRepo.findAll();
   }

   public void save(Kategorie kat) {
      kategorieRepo.save(kat);
   }
}
