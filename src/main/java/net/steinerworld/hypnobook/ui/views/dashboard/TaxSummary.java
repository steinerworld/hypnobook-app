package net.steinerworld.hypnobook.ui.views.dashboard;

import java.text.DecimalFormat;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import net.steinerworld.hypnobook.domain.TaxPeriodState;

public class TaxSummary extends HorizontalLayout {
   private static final DecimalFormat DF = new DecimalFormat("#,##0.00");

   public TaxSummary(TaxPeriodState taxStatus, double ingoing, double outgoing) {
      setPadding(true);
      setWidthFull();
      setJustifyContentMode(JustifyContentMode.CENTER);
      setAlignItems(Alignment.CENTER);

      // Total Einnahmen
      Span inBadge = new Span(createBadgeIcon(VaadinIcon.ARROW_RIGHT), new Span(DF.format(ingoing)));
      inBadge.getElement().getThemeList().add("badge success");

      // Total Ausgaben
      Span outBadge = new Span(createBadgeIcon(VaadinIcon.ARROW_LEFT), new Span(DF.format(outgoing)));
      outBadge.getElement().getThemeList().add("badge error");

      // Differenz
      double diff = ingoing - outgoing;
      Span diffBadge = new Span();
      if (diff < 0) {
         diffBadge.add(createBadgeIcon(VaadinIcon.ARROW_DOWN), new Span(DF.format(diff)));
         diffBadge.getElement().getThemeList().add("badge error primary");
      } else if (diff > 0) {
         diffBadge.add(createBadgeIcon(VaadinIcon.ARROW_UP), new Span(DF.format(diff)));
         diffBadge.getElement().getThemeList().add("badge success primary");
      } else {
         diffBadge.add(createBadgeIcon(VaadinIcon.CIRCLE_THIN), new Span(DF.format(diff)));
         diffBadge.getElement().getThemeList().add("badge primary");
      }

      // Status der Steuerperiode
      Span taxStatusBadge = new Span(taxStatus.getCaption());
      if (taxStatus == TaxPeriodState.GESCHLOSSEN) {
         taxStatusBadge.getElement().getThemeList().add("badge error");
      } else if (taxStatus == TaxPeriodState.AKTIV) {
         taxStatusBadge.getElement().getThemeList().add("badge success");
      } else {
         taxStatusBadge.getElement().getThemeList().add("badge");
      }

      add(inBadge, outBadge, diffBadge, taxStatusBadge);
   }

   private Icon createBadgeIcon(VaadinIcon vaadinIcon) {
      Icon icon = vaadinIcon.create();
      icon.getStyle().set("padding", "var(--lumo-space-xs");
      return icon;
   }

}
