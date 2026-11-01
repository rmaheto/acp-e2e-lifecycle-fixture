package fixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TextUtilsTest {

  @Test
  void shout_whenNull_returnsEmpty() {
    assertEquals("", TextUtils.shout(null));
  }

  @Test
  void shout_whenText_returnsUpperCase() {
    assertEquals("HELLO", TextUtils.shout("hello"));
  }
}
