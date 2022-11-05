package net.steinerworld.hypnobook.services;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class VersionService {
   private static final Logger LOGGER = LoggerFactory.getLogger(VersionService.class);

   @Getter private String version;
   @Getter private String buildTime;

   @PostConstruct
   public void initialize() {
      try (InputStream stream = getClass().getResourceAsStream("/version.txt")) {
         if (stream != null) {
            String[] row = new String(stream.readAllBytes()).split(";");
            version = row[0];
            buildTime = row[1];
         } else {
            version = "! dev mode !";
            buildTime = "now";
         }
      } catch (IOException e) {
         LOGGER.info("cannot read version file", e);
         version = "Version ?";
         buildTime = "now";
      }
   }

   public String getPresentation() {
      return String.format("Version %s - %s", version, buildTime);
   }


}
