package net.steinerworld.hypnobook.domain;

public enum AccountingType {
   EINNAHME("Einnahme"), AUSGABE("Ausgabe");

   private final String caption;

   AccountingType(String caption) {
      this.caption = caption;
   }

   public String getCaption() {
      return caption;
   }
}
