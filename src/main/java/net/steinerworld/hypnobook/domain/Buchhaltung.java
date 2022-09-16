package net.steinerworld.hypnobook.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * A Buchhaltung.
 */
@Entity
@Table(name = "buchhaltung")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter @Accessors(chain = true)
@ToString
public class Buchhaltung implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "buchhaltung_seq")
   @SequenceGenerator(name = "buchhaltung_seq", sequenceName = "buchhaltung_seq")
   @Column(name = "id")
   private Long id;

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

   @ManyToOne()
   @JoinColumn(name = "kategorie_id", referencedColumnName = "id")
   @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   private Kategorie kategorie;

   @ManyToOne(optional = false)
   @JoinColumn(name = "steuerperiode_id", nullable = false, referencedColumnName = "id")
   @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   private Steuerperiode steuerperiode;

   @Override public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Buchhaltung that = (Buchhaltung) o;
      return id.equals(that.id) && buchungsdatum.equals(that.buchungsdatum) && Objects.equals(einnahme, that.einnahme)
            && Objects.equals(eingangsdatum, that.eingangsdatum) && Objects.equals(ausgabe, that.ausgabe)
            && Objects.equals(belegNr, that.belegNr) && Objects.equals(text, that.text) && Objects.equals(kategorie,
            that.kategorie) && steuerperiode.equals(that.steuerperiode);
   }

   @Override public int hashCode() {
      return Objects.hash(id, buchungsdatum, einnahme, eingangsdatum, ausgabe, belegNr, text, kategorie, steuerperiode);
   }
}
