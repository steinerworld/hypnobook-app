package net.steinerworld.hypnobook.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * A Kategorie.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter
public class Kategorie implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
   @SequenceGenerator(name = "sequenceGenerator")
   @Column(name = "id")
   private Long id;

   @Column(name = "name")
   private String name;

   @Column(name = "bezeichnung")
   private String bezeichnung;

   //   @ManyToOne
   //   @JsonIgnoreProperties(value = {"kategories", "periodes"}, allowSetters = true)
   //   private Buchhaltung buchhaltung;
}
