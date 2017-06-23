package de.tum.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomUtil {

  private static final SecureRandom random = new SecureRandom();
  private static final int certainty = 0;

  public static String generatePassword() {
    return generate(12);
  }

  public static String generate(int length) {
    // generates prime numbers, but we don't care about prime numbers, therefore certainty is 0
    // 32 digits are equal to 5 bits
    return new BigInteger(length * 5, certainty, random).toString(32);
  }

}
