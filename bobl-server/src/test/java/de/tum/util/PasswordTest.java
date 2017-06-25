package de.tum.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PasswordTest {

  @Test
  public void simple() {
    int[] lenghts = {5, 10, 15};

    for (int length : lenghts) {
      String pw = RandomUtil.generate(length);
      String hash = Password.getSaltedHash(pw);

      assertEquals(89, hash.length());
      assertTrue(Password.check(pw, hash));

    }

  }

}
