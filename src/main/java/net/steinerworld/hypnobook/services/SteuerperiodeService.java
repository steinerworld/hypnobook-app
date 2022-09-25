package net.steinerworld.hypnobook.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Steuerperiode;
import net.steinerworld.hypnobook.domain.SteuerperiodeState;
import net.steinerworld.hypnobook.repository.SteuerperiodeRepository;

@Service
@RequiredArgsConstructor
public class SteuerperiodeService {
   private final SteuerperiodeRepository repository;

   public Optional<Steuerperiode> get(Long id) {
      return repository.findById(id);
   }

   public List<Steuerperiode> findAll() {
      return repository.findAll();
   }

   public Steuerperiode save(Steuerperiode periode) {
      return repository.save(periode);
   }

   public Optional<Steuerperiode> findActive() {
      return Optional.ofNullable(repository.findByStatusEquals(SteuerperiodeState.AKTIV));
   }
}
