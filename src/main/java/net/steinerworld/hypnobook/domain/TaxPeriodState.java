package net.steinerworld.hypnobook.domain;

/**
 * The TaxPeriodState enumeration.
 */
public enum TaxPeriodState {
   ERSTELLT("Erstellt"), AKTIV("Aktiv"), GESCHLOSSEN("Geschlossen");

   private final String caption;

   TaxPeriodState(String caption) {
      this.caption = caption;
   }

   public String getCaption() {
      return caption;
   }
}
