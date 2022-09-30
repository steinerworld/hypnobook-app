package net.steinerworld.hypnobook.exceptions;

public class MaloneyException extends RuntimeException {
   public MaloneyException(String message) {
      super(message);
   }

   public MaloneyException(String message, Throwable cause) {
      super(message, cause);
   }
}
