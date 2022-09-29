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
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * A TaxPeriod.
 */
@Entity
@Table(name = "tax_period")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter @Accessors(chain = true)
@ToString
public class TaxPeriod implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id @GeneratedValue(generator = "system-uuid")
   @GenericGenerator(name = "system-uuid", strategy = "uuid")
   @Column(name = "id")
   private UUID id;

   @Column(name = "jahresbezeichnung")
   private String jahresbezeichnung;

   @Column(name = "von")
   private LocalDate von;

   @Column(name = "bis")
   private LocalDate bis;

   @Enumerated(EnumType.STRING)
   @Column(name = "status")
   private TaxPeriodState status;

   @Override public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      TaxPeriod that = (TaxPeriod) o;
      return Objects.equals(jahresbezeichnung, that.jahresbezeichnung);
   }

   @Override public int hashCode() {
      return Objects.hash(jahresbezeichnung);
   }
}
