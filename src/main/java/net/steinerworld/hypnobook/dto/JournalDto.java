package net.steinerworld.hypnobook.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.steinerworld.hypnobook.domain.Accounting;

@Getter @Setter
@Accessors(chain = true)
@ToString
public class JournalDto {
   private String currentYearCaption;
   private List<Accounting> accountingList;
}
