package net.steinerworld.hypnobook.domain;

public enum AccountingType {
   EINNAHME("Einnahme", "E-"), AUSGABE("Ausgabe", "A-");

   private final String caption;
   private final String belegPrefix;

   AccountingType(String caption, String belegPrefix) {
      this.caption = caption;
      this.belegPrefix = belegPrefix;
   }

   public String getCaption() {
      return caption;
   }

   public String getBelegPrefix() {
      return belegPrefix;
   }
}
