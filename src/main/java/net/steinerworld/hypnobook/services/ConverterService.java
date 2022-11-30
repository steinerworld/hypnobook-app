package net.steinerworld.hypnobook.services;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class ConverterService {
   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.forLanguageTag("de-CH")));

   public String localDateToString(LocalDate date) {
      return DATE_TIME_FORMATTER.format(date);
   }

   public String doubleToChf(double val) {
      return DECIMAL_FORMAT.format(val);
   }
}
