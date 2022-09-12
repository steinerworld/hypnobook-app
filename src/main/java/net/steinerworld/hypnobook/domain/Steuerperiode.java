package net.steinerworld.hypnobook.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * A Steuerperiode.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter
public class Steuerperiode implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
   @SequenceGenerator(name = "sequenceGenerator")
   @Column(name = "id")
   private Long id;

   @Column(name = "jahresbezeichnung")
   private String jahresbezeichnung;

   @Column(name = "von")
   private LocalDate von;

   @Column(name = "bis")
   private LocalDate bis;

   @Enumerated(EnumType.STRING)
   @Column(name = "status")
   private SteuerperiodeState status;

   @ManyToOne
   @JsonIgnoreProperties(value = {"kategories", "periodes"}, allowSetters = true)
   private Buchhaltung buchhaltung;
}
