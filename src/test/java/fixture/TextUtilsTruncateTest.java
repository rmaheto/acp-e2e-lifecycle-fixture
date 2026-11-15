package fixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TextUtilsTruncateTest {

  @Test
  void truncate_whenTextIsNull_returnsNull() {
    assertNull(TextUtils.truncate(null, 3));
  }

  @Test
  void truncate_whenTextIsShort_returnsTextUnchanged() {
    assertEquals("cat", TextUtils.truncate("cat", 5));
  }

  @Test
  void truncate_whenTextHasExactLength_returnsTextUnchanged() {
    assertEquals("hello", TextUtils.truncate("hello", 5));
  }

  @Test
  void truncate_whenTextIsLonger_returnsPrefix() {
    assertEquals("hel", TextUtils.truncate("hello", 3));
  }

  @Test
  void truncate_whenMaxLengthIsZero_returnsEmptyPrefix() {
    assertEquals("", TextUtils.truncate("hello", 0));
  }

  @Test
  void truncate_whenMaxLengthIsNegative_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> TextUtils.truncate("hello", -1));
  }
}
