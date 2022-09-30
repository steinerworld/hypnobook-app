package net.steinerworld.hypnobook.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(chain = true)
@ToString
public class BalanceDto {
   private String currentYearCaption;
   private String lastYearCaption;
   private String sumTotalIngoingCurrentYear;
   private String sumTotalIngoingLastYear;
   private String sumTotalOutgoingCurrentYear;
   private String sumTotalOutgoingLastYear;
   private String resultCaption;
   private String resultCurrentYear;
   private String resultLastYear;
   private List<SumByCategory> categoryList;


   @Getter @Setter
   @Accessors(chain = true)
   @AllArgsConstructor
   @ToString
   public static class SumByCategory {
      private String category;
      private String sumByCategoryCurrentYear;
      private String sumByCategoryLastYear;
   }
}
