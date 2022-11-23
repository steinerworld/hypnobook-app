package net.steinerworld.hypnobook.ui.views.dashboard;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.FillBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.LabelsBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;

public class SumPerMonthBarChart extends ApexChartsBuilder {

   public SumPerMonthBarChart(List<Double> ingoing, List<Double> outgoing) {
      Series<Double> inSerie = new Series<>();
      inSerie.setName("Einnahmen");
      inSerie.setData(ingoing.toArray(Double[]::new));

      Series<Double> outSerie = new Series<>();
      outSerie.setName("Ausgaben");
      outSerie.setData(outgoing.toArray(Double[]::new));

      String[] months = new DateFormatSymbols().getShortMonths();

      withChart(ChartBuilder.get()
            .withType(Type.BAR)
            .withForeColor("var(--lumo-body-text-color)")
            .build())
            .withPlotOptions(PlotOptionsBuilder.get()
                  .withBar(BarBuilder.get()
                        .withHorizontal(false)
                        .withColumnWidth("55%")
                        .build())
                  .build())
            .withDataLabels(DataLabelsBuilder.get()
                  .withEnabled(false).build())
            .withStroke(StrokeBuilder.get()
                  .withShow(true)
                  .withWidth(2.0)
                  .withColors("transparent")
                  .build())
            .withSeries(inSerie, outSerie)
            .withYaxis(YAxisBuilder.get()
                  .withTitle(TitleBuilder.get()
                        .withText("CHF")
                        .build())
                  .withLabels(LabelsBuilder.get()
                        .withFormatter("function(val) {\n"
                              + "  return val.toFixed(0)\n"
                              + "}")
                        .build())
                  .build())
            .withXaxis(XAxisBuilder.get()
                  .withCategories(Arrays.copyOf(months, months.length - 1))
                  .build())
            .withFill(FillBuilder.get()
                  .withOpacity(1.0).build());
   }
}
