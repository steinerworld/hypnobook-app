package net.steinerworld.hypnobook.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * A Buchhaltung.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter
public class Buchhaltung implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
   @SequenceGenerator(name = "sequenceGenerator")
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

   @OneToMany(mappedBy = "buchhaltung")
   @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   @JsonIgnoreProperties(value = {"buchhaltung"}, allowSetters = true)
   private Set<Kategorie> kategories = new HashSet<>();

   @OneToMany(mappedBy = "buchhaltung")
   @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   @JsonIgnoreProperties(value = {"buchhaltung"}, allowSetters = true)
   private Set<Steuerperiode> periodes = new HashSet<>();
}
