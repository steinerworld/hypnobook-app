package net.steinerworld.hypnobook.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter @Accessors(chain = true)
@ToString
public class Category implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id @GeneratedValue
   @Column(name = "id")
   private UUID id;

   @Column(name = "bezeichnung")
   private String bezeichnung;

   @Column(name = "info", length = 1024)
   private String info;

   @Override public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Category category = (Category) o;
      return id.equals(category.id) && bezeichnung.equals(category.bezeichnung);
   }

   @Override public int hashCode() {
      return Objects.hash(id, bezeichnung);
   }
}
