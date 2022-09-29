package net.steinerworld.hypnobook.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * A Accounting.
 */
@Entity
@Table(name = "accounting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter @Accessors(chain = true)
@ToString
public class Accounting implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id @GeneratedValue
   @Column(name = "id")
   private UUID id;

   @Column(name = "buchungsdatum")
   private LocalDate buchungsdatum;

   @Column(name = "einnahme")
   private Double einnahme;

   @Column(name = "eingangsdatum")
   private LocalDate eingangsdatum;

   @Column(name = "ausgabe")
   private Double ausgabe;

   @Column(name = "beleg_nr")
   private String belegNr;

   @Column(name = "text")
   private String text;

   @Enumerated(EnumType.STRING)
   @Column(name = "accounting_type")
   private AccountingType accountingType;

   @ManyToOne()
   @JoinColumn(name = "category_id", referencedColumnName = "id")
   @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   private Category category;

   @ManyToOne(optional = false)
   @JoinColumn(name = "tax_period_id", nullable = false, referencedColumnName = "id")
   @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   private TaxPeriod taxPeriod;

   @Override public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Accounting that = (Accounting) o;
      return id.equals(that.id) && buchungsdatum.equals(that.buchungsdatum) && Objects.equals(einnahme, that.einnahme)
            && Objects.equals(eingangsdatum, that.eingangsdatum) && Objects.equals(ausgabe, that.ausgabe)
            && Objects.equals(belegNr, that.belegNr) && Objects.equals(text, that.text) && accountingType == that.accountingType
            && category.equals(that.category) && taxPeriod.equals(that.taxPeriod);
   }

   @Override public int hashCode() {
      return Objects.hash(id, buchungsdatum, einnahme, eingangsdatum, ausgabe, belegNr, text, accountingType, category, taxPeriod);
   }
}
