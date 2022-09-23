package net.steinerworld.hypnobook.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class DateConverter {
   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

   public String localDateToString(LocalDate date) {
      return DATE_TIME_FORMATTER.format(date);
   }
}
