package net.steinerworld.hypnobook.ui.views.dashboard;

import java.util.Map;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;

public class SumPerCategoryPieChart extends ApexChartsBuilder {

   public SumPerCategoryPieChart(Map<String, Double> sums) {
      withChart(ChartBuilder.get().withType(Type.PIE).build())
            .withLabels(sums.keySet().toArray(String[]::new))
            .withLegend(LegendBuilder.get()
                  .withPosition(Position.RIGHT)
                  .build())
            .withSeries(sums.values().toArray(Double[]::new))
            .withResponsive(ResponsiveBuilder.get()
                  .withBreakpoint(480.0)
                  .withOptions(OptionsBuilder.get()
                        .withLegend(LegendBuilder.get()
                              .withPosition(Position.BOTTOM)
                              .build())
                        .build())
                  .build());
   }
}
