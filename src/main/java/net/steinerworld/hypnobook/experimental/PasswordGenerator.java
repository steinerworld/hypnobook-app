package net.steinerworld.hypnobook.experimental;

import java.util.Scanner;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {
   private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

   public static void main(String[] args) {
      new PasswordGenerator().generate();
      System.out.println("Done - EXIT");
   }

   private void generate() {
      boolean oneMoreTime = true;
      System.out.println("To exit, type 'exit'");
      while (oneMoreTime) {
         String pwd = readInput();
         if (pwd != null && !pwd.equals("exit")) {
            String encode = passwordEncoder.encode(pwd);
            System.out.println("You entered password '" + pwd + "', encoded: '" + encode + "'");
         } else {
            oneMoreTime = false;
         }
      }
   }

   private String readInput() {
      System.out.print("Enter new Password: ");
      return new Scanner(System.in).nextLine();
   }

}
