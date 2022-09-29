package net.steinerworld.hypnobook.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;
import net.steinerworld.hypnobook.repository.TaxPeriodRepository;

@Service
@RequiredArgsConstructor
public class TaxPeriodService {
   private static final Logger LOGGER = LoggerFactory.getLogger(TaxPeriodService.class);
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

   public void changeActiveTaxPeriod(TaxPeriod current, TaxPeriod future) {
      current.setStatus(TaxPeriodState.GESCHLOSSEN);
      save(current);
      LOGGER.info("TaxPeriod closed: {}", current);
      future.setStatus(TaxPeriodState.AKTIV);
      save(future);
      LOGGER.info("TaxPeriod active: {}", future);
   }

   public void createBalanceSheet(TaxPeriod periode) {
      LOGGER.info("Balance, ich komme");
   }
}
