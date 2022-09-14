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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * A Steuerperiode.
 */
@Entity
@Table(name = "steuerperiode")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter
public class Steuerperiode implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "periode_seq")
   @SequenceGenerator(name = "periode_seq", sequenceName = "periode_seq")
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
}
