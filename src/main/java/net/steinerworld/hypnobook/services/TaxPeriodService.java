package net.steinerworld.hypnobook.services;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.notification.Notification;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Accounting;
import net.steinerworld.hypnobook.domain.AccountingType;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;
import net.steinerworld.hypnobook.dto.BalanceDto;
import net.steinerworld.hypnobook.exceptions.MaloneyException;
import net.steinerworld.hypnobook.repository.TaxPeriodRepository;

@Service
@RequiredArgsConstructor
public class TaxPeriodService {
   private static final Logger LOGGER = LoggerFactory.getLogger(TaxPeriodService.class);
   private static final DecimalFormat DF = new DecimalFormat("#,##0.00");

   private final TaxPeriodRepository taxRepository;
   private final AccountingService accountingService;
   private final CategoryService catService;
   private final BalanceSheetService balanceService;

   public Optional<TaxPeriod> get(UUID id) {
      return taxRepository.findById(id);
   }

   public List<TaxPeriod> findAll() {
      return taxRepository.findAll();
   }

   public TaxPeriod save(TaxPeriod periode) {
      return taxRepository.save(periode);
   }

   public Optional<TaxPeriod> findActive() {
      return Optional.ofNullable(taxRepository.findByStatusEquals(TaxPeriodState.AKTIV));
   }

   public Optional<TaxPeriod> findByBusinessYear(int year) {
      return taxRepository.findByGeschaeftsjahr(year);
   }

   public void changeActiveTaxPeriod(TaxPeriod current, TaxPeriod future) {
      current.setStatus(TaxPeriodState.GESCHLOSSEN);
      save(current);
      LOGGER.info("TaxPeriod closed: {}", current);
      future.setStatus(TaxPeriodState.AKTIV);
      save(future);
      LOGGER.info("TaxPeriod active: {}", future);
   }

   public ByteArrayOutputStream streamBalanceSheet(TaxPeriod periode) {
      LOGGER.info("Balance, ich komme");
      if (periode.getStatus() != TaxPeriodState.GESCHLOSSEN) {
         Notification.show("Nur f??r geschlossene Stuerperioden");
         throw new MaloneyException("Nur f??r geschlossene Stuerperioden");
      }

      Optional<TaxPeriod> maybeLast = findByBusinessYear(periode.getGeschaeftsjahr() - 1);
      BalanceDto balanceDto = balanceService.createDto(periode, maybeLast);
      List<BalanceDto.SumByCategory> catList = catService.findAll().stream()
            .map(cat -> balanceService.createCatSummary(cat, periode, maybeLast))
            .collect(Collectors.toList());
      balanceDto.setCategoryList(catList);

      return balanceService.streamBalancePdfSheet(balanceDto);
   }

   public Map<String, Double> sumByTaxAndCats(TaxPeriod tax) {
      return catService.findAll().stream()
            .collect(Collectors.toMap(Category::getBezeichnung, cat -> accountingService.sumAusgabeInTaxPeriodAndCategory(tax, cat)));
   }

   public List<Double> outgoingSumPerMonthByTax(TaxPeriod tax) {
      List<Double> monthList = Arrays.stream(Month.values()).map(m -> 0.0).collect(Collectors.toList());
      accountingService.findAllSortedInPeriode(tax, Sort.Direction.DESC).stream()
            .filter(ac -> ac.getAccountingType() == AccountingType.AUSGABE)
            .collect(Collectors.groupingBy(ac -> ac.getBuchungsdatum().getMonthValue(), Collectors.summingDouble(Accounting::getAusgabe)))
            .forEach((monthNr, sum) -> monthList.set(monthNr - 1, sum));
      return monthList;
   }

   public List<Double> ingoingSumPerMonthByTax(TaxPeriod tax) {
      List<Double> monthList = Arrays.stream(Month.values()).map(m -> 0.0).collect(Collectors.toList());
      accountingService.findAllSortedInPeriode(tax, Sort.Direction.DESC).stream()
            .filter(ac -> ac.getAccountingType() == AccountingType.EINNAHME)
            .collect(Collectors.groupingBy(ac -> ac.getBuchungsdatum().getMonthValue(), Collectors.summingDouble(Accounting::getEinnahme)))
            .forEach((monthNr, sum) -> monthList.set(monthNr - 1, sum));
      return monthList;
   }

}
