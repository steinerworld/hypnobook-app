package net.steinerworld.hypnobook.experimental;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Prototype {
   private static final Logger LOGGER = LoggerFactory.getLogger(Prototype.class);

   private Supplier<String> sp;

   public static void main(String... args) {
      LOGGER.info("in main");
      Prototype pr = new Prototype();
      pr.doit(Prototype::getMe);
   }

   public static String getMe() {
      LOGGER.info("in getMe");
      return "I got you";
   }

   public void doit(Supplier<String> sp) {
      String s = sp.get();
      LOGGER.info("doit is {}", s);
   }


}
