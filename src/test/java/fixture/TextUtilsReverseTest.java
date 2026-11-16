package fixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TextUtilsReverseTest {

  @Test
  void reverse_whenTextIsNull_returnsEmpty() {
    assertEquals("", TextUtils.reverse(null));
  }

  @Test
  void reverse_whenTextIsEmpty_returnsEmpty() {
    assertEquals("", TextUtils.reverse(""));
  }

  @Test
  void reverse_whenTextHasOneCharacter_returnsThatCharacter() {
    assertEquals("a", TextUtils.reverse("a"));
  }

  @Test
  void reverse_whenTextHasMultipleCharacters_returnsReversedText() {
    assertEquals("olleh", TextUtils.reverse("hello"));
  }
}
