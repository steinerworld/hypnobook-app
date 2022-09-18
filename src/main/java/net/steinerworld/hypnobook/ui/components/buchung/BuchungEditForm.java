package net.steinerworld.hypnobook.ui.components.buchung;

import com.vaadin.flow.component.formlayout.FormLayout;

import lombok.Builder;

@Builder
public class BuchungEditForm extends FormLayout {
   //   private BuchungType type;
   //   private BeanValidationBinder<Buchung> binder;
   //   private Supplier<List<Kategorie>> kategorieList;
   //   private Supplier<List<Steuerperiode>> periodeList;
   //
   //   public BuchungEditForm get() {
   //      setResponsiveSteps(
   //            new FormLayout.ResponsiveStep("0", 1),
   //            new FormLayout.ResponsiveStep("500px", 3)
   //      );
   //      if (type == BuchungType.AUSGABE) {
   //         add(buildBelegNrComponent(),
   //               buildBetragAusgabeComponent(),
   //               buildBuchungsdatumComponent(),
   //               buildKategorieComponent(kategorieList.get()),
   //               buildTextComponent());
   //      } else if (type == BuchungType.EINNAHME) {
   //         add(buildBelegNrComponent(),
   //               buildBetragEinnahmeComponent(),
   //               buildBuchungsdatumComponent(),
   //               buildZahlungseingangComponent(),
   //               buildTextComponent());
   //      }
   //      return this;
   //   }
   //
   //   private TextField buildTextComponent() {
   //      TextField tfText = new TextField("Text");
   //      setColspan(tfText, 2);
   //      binder.forField(tfText).bind(Buchung::getText, Buchung::setText);
   //      return tfText;
   //   }
   //
   //   private Select<Kategorie> buildKategorieComponent(List<Kategorie> allKategorie) {
   //      Select<Kategorie> seKategorie = new Select<>();
   //      seKategorie.setLabel("Kategorie");
   //      seKategorie.setItemLabelGenerator(Kategorie::getName);
   //      seKategorie.setItems(allKategorie);
   //      binder.forField(seKategorie).bind(Buchung::getKategorie, Buchung::setKategorie);
   //      return seKategorie;
   //   }
   //
   //   private DatePicker buildBuchungsdatumComponent() {
   //      DatePicker dpBuchungsdatum = new DatePicker("Datum");
   //      binder.forField(dpBuchungsdatum)
   //            .withValidator(this::inValidPeriode, "Buchung in geschlossener oder noch nicht erfasster Steuerperiode")
   //            .bind(Buchung::getBuchungsdatum, Buchung::setBuchungsdatum);
   //      return dpBuchungsdatum;
   //   }
   //
   //   private boolean inValidPeriode(LocalDate date) {
   //      return periodeList.get().stream()
   //            .filter(periode -> periode.getStatus() != SteuerperiodeState.GESCHLOSSEN)
   //            .filter(periode -> date.isAfter(periode.getVon()))
   //            .anyMatch(periode -> date.isBefore(periode.getBis()));
   //   }
   //
   //   private NumberField buildBetragAusgabeComponent() {
   //      NumberField nfBetrag = new NumberField("Betrag in CHF");
   //      Div chfSuffix = new Div();
   //      chfSuffix.setText("CHF");
   //      nfBetrag.setSuffixComponent(chfSuffix);
   //      binder.forField(nfBetrag).bind(Buchung::getAusgabe, Buchung::setAusgabe);
   //      return nfBetrag;
   //   }
   //
   //   private NumberField buildBetragEinnahmeComponent() {
   //      NumberField nfBetrag = new NumberField("Betrag in CHF");
   //      Div chfSuffix = new Div();
   //      chfSuffix.setText("CHF");
   //      nfBetrag.setSuffixComponent(chfSuffix);
   //      binder.forField(nfBetrag).bind(Buchung::getEinnahme, Buchung::setEinnahme);
   //      return nfBetrag;
   //   }
   //
   //   private TextField buildBelegNrComponent() {
   //      TextField tfBelegNr = new TextField("Beleg-Nr.");
   //      binder.forField(tfBelegNr).bind(Buchung::getBelegNr, Buchung::setBelegNr);
   //      return tfBelegNr;
   //   }
   //
   //   private Select<Steuerperiode> buildSteuerperiodeComponent(List<Steuerperiode> allPeriode) {
   //      Select<Steuerperiode> sePeriode = new Select<>();
   //      sePeriode.setLabel("Steuerperiode");
   //      sePeriode.setItemLabelGenerator(Steuerperiode::getJahresbezeichnung);
   //      sePeriode.setItems(allPeriode);
   //      binder.forField(sePeriode).bind(Buchung::getSteuerperiode, Buchung::setSteuerperiode);
   //      return sePeriode;
   //   }
   //
   //   private DatePicker buildZahlungseingangComponent() {
   //      DatePicker dpZahlungseingang = new DatePicker("Zahlungseingang");
   //      binder.forField(dpZahlungseingang).bind(Buchung::getEingangsdatum, Buchung::setEingangsdatum);
   //      return dpZahlungseingang;
   //   }
   //
   //
}
