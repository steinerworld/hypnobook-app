package net.steinerworld.hypnobook.generator;

import java.time.LocalDate;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.AppUser;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.domain.TaxPeriod;
import net.steinerworld.hypnobook.domain.TaxPeriodState;
import net.steinerworld.hypnobook.repository.AppUserRepository;
import net.steinerworld.hypnobook.repository.CategoryRepository;
import net.steinerworld.hypnobook.repository.TaxPeriodRepository;

@Component
@RequiredArgsConstructor
public class InitializeData implements CommandLineRunner {
   private static final Logger LOGGER = LoggerFactory.getLogger(InitializeData.class);

   private final PasswordEncoder passwordEncoder;
   private final AppUserRepository userRepo;
   private final TaxPeriodRepository taxRepo;
   private final CategoryRepository categoryRepo;

   @Override public void run(String... args) throws Exception {
      createUser();
      createTaxPeriod();
      createCategories();
   }

   private void createCategories() {
      if (categoryRepo.count() == 0) {
         LOGGER.info("... generating Category...");
         saveCategory("Direkter Aufwand",
               "Z.B.: Material, Waren, Fremdleistungen. Hier erfassen Sie alle Ausgaben, die direkt für die Erledigung von Kundenaufträgen benötigt werden. Wenn Sie z.B. als Grafiker ein lizenziertes Foto für ein Kundenprojekt kaufen oder als Fotograf die Materialkosten für die Fotoentwicklung oder den Bilderrahmen.");
         saveCategory("AHV/IV/EO-Beiträge", "1. Säule. Ihre persönlichen AHV/IV/EO-Beiträge.");
         saveCategory("BVG/Pensionskassen-Beiträge",
               "2. Säule, nur zu 50%. Da die 2. Säule für Einzelunternehmen freiwillig ist, haben die meisten Selbständigen hier keine Ausgaben. Wenn Sie diese aber haben, dann dürfen Sie lediglich 50% der tatsächlich bezahlten Beiträge im HypnoBook angeben. Die anderen 50% bezahlen Sie als Privatperson, nicht als Unternehmer.");
         saveCategory("Übrige Sozialversicherungen",
               "Unfall, Krankentaggeld. Private Versicherungen oder Krankenkasse dürfen hingegen nicht erfasst werden.");
         saveCategory("Miete Praxis/Büro",
               "Wenn Sie ein Büro gemietet haben, ist die Ausgabe eindeutig. Wenn Sie aber Zuhause arbeiten, können Sie einen Teil der privaten Miete abziehen. Wie gross dieser Anteil sein darf, ist von Kanton zu Kanton unterschiedlich.");
         saveCategory("Büromaterial, IT, Telefon, Porti",
               "Ein Hinweis speziell zur EDV, wenn Sie beispielsweise einen Laptop kaufen: Als Faustregel gilt, dass Beträge bis CHF 1000.- ganz normal als Ausgaben erfasst werden können. Grössere Ausgaben müssen abgeschrieben werden. Das heisst, über 2 oder mehr Jahre wird lediglich ein Teil des gesamten Betrages als Ausgabe erfasst.");
         saveCategory("Bankspesen", "Die Kontogebühren Ihres Geschäftskontos, die meistens jedes Quartal anfallen.");
         saveCategory("Fahrzeug/Benzin", "Ausgaben für Ihr Auto.");
         saveCategory("Reparatur und Unterhalt",
               "Für Selbständige betrifft dies praktisch nur das Auto, da Sie wahrscheinlich keine grossen Maschinen oder Gebäudeunterhalt haben.");
         saveCategory("Reisespesen", "Z.B. Flug- oder Zug-Tickets.");
         saveCategory("Repräsentation",
               "In diese Kategorie fallen beispielsweise Kundengeschenke oder Ausgaben für Geschäftsessen mit potenziellen Kunden oder Geschäftspartnern.");
         saveCategory("Werbung/Marketing", "Ausgaben für z.B. Visitenkarten, Inserate, Website oder Google AdWords.");
         saveCategory("Weiterbildung", "Zum Beispiel besuchte Kurse oder Seminare.");
         saveCategory("Alle übrigen Geschäftsaufwände",
               "Alles, was nicht in die anderen Kategorien fällt. Normalerweise sollten hier nicht all zu viele Ausgaben anfallen. Zum Beispiel die jährlichen Kehrichtgrundgebühren für Betriebe der Gemeinde.");
      } else {
         LOGGER.info("Using existing Category");
      }
   }

   private void saveCategory(String bezeichnung, String info) {
      Category cat = new Category()
            .setBezeichnung(bezeichnung)
            .setInfo(info);
      categoryRepo.save(cat);
   }

   private void createTaxPeriod() {
      if (taxRepo.count() == 0) {
         LOGGER.info("... generating TaxPeriod...");
         TaxPeriod periode0 = new TaxPeriod()
               .setGeschaeftsjahr(2021)
               .setVon(LocalDate.of(2021, 1, 1))
               .setBis(LocalDate.of(2021, 12, 31))
               .setStatus(TaxPeriodState.GESCHLOSSEN);
         taxRepo.save(periode0);
         TaxPeriod periode1 = new TaxPeriod()
               .setGeschaeftsjahr(2022)
               .setVon(LocalDate.of(2022, 1, 1))
               .setBis(LocalDate.of(2022, 12, 31))
               .setStatus(TaxPeriodState.AKTIV);
         taxRepo.save(periode1);
         TaxPeriod periode2 = new TaxPeriod()
               .setGeschaeftsjahr(2023)
               .setVon(LocalDate.of(2023, 1, 1))
               .setBis(LocalDate.of(2023, 12, 31))
               .setStatus(TaxPeriodState.ERSTELLT);
         taxRepo.save(periode2);
         LOGGER.info("Generated demo TaxPeriod data");
      } else {
         LOGGER.info("Using existing TaxPeriod");
      }
   }

   private void createUser() {
      if (userRepo.count() == 0) {
         LOGGER.info("... generating User admin...");
         AppUser admin = new AppUser()
               .setName("Michael Steiner")
               .setUsername("admin")
               .setPassword(passwordEncoder.encode("nimdA42+"))
               .setProfilePicture(Base64.getDecoder().decode(
                     "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"))
               .setRoles("ADMIN")
               .setTheme("light");
         userRepo.save(admin);

         AppUser karin = new AppUser()
               .setName("Karin Steiner")
               .setUsername("karin")
               .setPassword(passwordEncoder.encode("MiKaNiJa333"))
               .setProfilePicture(Base64.getDecoder().decode(
                     "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"))
               .setRoles("USER")
               .setTheme("light");
         userRepo.save(karin);
      } else {
         LOGGER.info("Using existing Useres");
      }
   }
}
