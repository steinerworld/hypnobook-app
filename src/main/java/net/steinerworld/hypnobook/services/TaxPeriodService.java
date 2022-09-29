package net.steinerworld.hypnobook.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;
import net.steinerworld.hypnobook.repository.TaxPeriodRepository;

@Service
@RequiredArgsConstructor
public class TaxPeriodService {
   private final TaxPeriodRepository repository;

   public Optional<TaxPeriod> get(UUID id) {
      return repository.findById(id);
   }

   public List<TaxPeriod> findAll() {
      return repository.findAll();
   }

   public TaxPeriod save(TaxPeriod periode) {
      return repository.save(periode);
   }

   public Optional<TaxPeriod> findActive() {
      return Optional.ofNullable(repository.findByStatusEquals(TaxPeriodState.AKTIV));
   }
}
