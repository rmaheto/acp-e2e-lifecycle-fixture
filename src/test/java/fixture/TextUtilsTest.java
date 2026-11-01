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

  @Test
  void normalizeDisplayName_whenNull_returnsEmpty() {
    assertEquals("", TextUtils.normalizeDisplayName(null));
  }

  @Test
  void normalizeDisplayName_whenBlank_returnsEmpty() {
    assertEquals("", TextUtils.normalizeDisplayName(" \t\n\r "));
  }

  @Test
  void normalizeDisplayName_whenSurroundedByWhitespace_trimsWhitespace() {
    assertEquals("Ada Lovelace", TextUtils.normalizeDisplayName(" \tAda Lovelace\n "));
  }

  @Test
  void normalizeDisplayName_whenWhitespaceIsRepeated_collapsesToSingleSpaces() {
    assertEquals("Ada Lovelace Byron", TextUtils.normalizeDisplayName("Ada \t\n Lovelace  Byron"));
  }

  @Test
  void normalizeDisplayName_whenContentHasNoWhitespace_preservesContent() {
    assertEquals("Ada-Lovelace_42!", TextUtils.normalizeDisplayName("Ada-Lovelace_42!"));
  }
}
