package de.tum.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomUtilTest {

  @Test
  public void simple() {
    int[] sizes = {3, 10, 20, 40};

    for (int size : sizes) {
      assertEquals(size, RandomUtil.generate(size).length());
    }
  }

  @Test
  public void passwordLength() {
    assertEquals(12, RandomUtil.generatePassword().length());
  }

}
