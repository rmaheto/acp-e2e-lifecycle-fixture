package fixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void countWords_whenTextIsNull_returnsZero() {
    assertEquals(0, TextUtils.countWords(null));
  }

  @Test
  void countWords_whenTextIsBlank_returnsZero() {
    assertEquals(0, TextUtils.countWords(" \t\n"));
  }

  @Test
  void countWords_whenTextIsOneWord_returnsOne() {
    assertEquals(1, TextUtils.countWords("hello"));
  }

  @Test
  void countWords_whenTextHasSeveralWords_returnsWordCount() {
    assertEquals(3, TextUtils.countWords("one two three"));
  }

  @Test
  void countWords_whenWordsHaveExtraWhitespace_returnsWordCount() {
    assertEquals(3, TextUtils.countWords("  one   two\tthree  "));
  }

  @Test
  void initials_whenTextIsNull_returnsEmpty() {
    assertEquals("", TextUtils.initials(null));
  }

  @Test
  void initials_whenTextIsBlank_returnsEmpty() {
    assertEquals("", TextUtils.initials(" \t\n"));
  }

  @Test
  void initials_whenTextIsOneWord_returnsUpperCaseFirstLetter() {
    assertEquals("A", TextUtils.initials("ada"));
  }

  @Test
  void initials_whenWordsAreSeparatedByMultipleSpaces_returnsConcatenatedInitials() {
    assertEquals("JRR", TextUtils.initials("john   ronald  reuel"));
  }

  @Test
  void repeat_whenTextIsNull_returnsEmpty() {
    assertEquals("", TextUtils.repeat(null, 3));
  }

  @Test
  void repeat_whenTimesIsZero_returnsEmpty() {
    assertEquals("", TextUtils.repeat("hello", 0));
  }

  @Test
  void repeat_whenTimesIsNegative_returnsEmpty() {
    assertEquals("", TextUtils.repeat("hello", -2));
  }

  @Test
  void repeat_whenTimesIsPositive_repeatsText() {
    assertEquals("hellohellohello", TextUtils.repeat("hello", 3));
  }

  @Test
  void isNullOrEmpty_whenValueIsNull_returnsTrue() {
    assertTrue(TextUtils.isNullOrEmpty(null));
  }

  @Test
  void isNullOrEmpty_whenValueIsEmpty_returnsTrue() {
    assertTrue(TextUtils.isNullOrEmpty(""));
  }

  @Test
  void isNullOrEmpty_whenValueIsWhitespaceOnly_returnsFalse() {
    assertFalse(TextUtils.isNullOrEmpty(" \t\n"));
  }

  @Test
  void isNullOrEmpty_whenValueIsNonEmpty_returnsFalse() {
    assertFalse(TextUtils.isNullOrEmpty("hello"));
  }

  @Test
  void isBlank_whenValueIsNull_returnsTrue() {
    assertTrue(TextUtils.isBlank(null));
  }

  @Test
  void isBlank_whenValueIsEmpty_returnsTrue() {
    assertTrue(TextUtils.isBlank(""));
  }

  @Test
  void isBlank_whenValueIsWhitespaceOnly_returnsTrue() {
    assertTrue(TextUtils.isBlank(" \t\n"));
  }

  @Test
  void isBlank_whenValueContainsNonWhitespace_returnsFalse() {
    assertFalse(TextUtils.isBlank(" hello "));
  }

  @Test
  void endsWith_whenSuffixMatches_returnsTrue() {
    assertTrue(TextUtils.endsWith("hello world", "world"));
  }

  @Test
  void endsWith_whenSuffixDoesNotMatch_returnsFalse() {
    assertFalse(TextUtils.endsWith("hello world", "hello"));
  }

  @Test
  void endsWith_whenSuffixIsEmpty_returnsTrue() {
    assertTrue(TextUtils.endsWith("hello", ""));
  }

  @Test
  void endsWith_whenValueIsNull_returnsFalse() {
    assertFalse(TextUtils.endsWith(null, "suffix"));
  }

  @Test
  void endsWith_whenSuffixIsNull_returnsFalse() {
    assertFalse(TextUtils.endsWith("value", null));
  }

  @Test
  void startsWith_whenPrefixMatches_returnsTrue() {
    assertTrue(TextUtils.startsWith("hello world", "hello"));
  }

  @Test
  void startsWith_whenPrefixDoesNotMatch_returnsFalse() {
    assertFalse(TextUtils.startsWith("hello world", "world"));
  }

  @Test
  void startsWith_whenPrefixIsEmpty_returnsTrue() {
    assertTrue(TextUtils.startsWith("hello", ""));
  }

  @Test
  void startsWith_whenValueIsNull_returnsFalse() {
    assertFalse(TextUtils.startsWith(null, "prefix"));
  }

  @Test
  void startsWith_whenPrefixIsNull_returnsFalse() {
    assertFalse(TextUtils.startsWith("value", null));
  }

  @Test
  void isPalindrome_whenTextIsNull_returnsFalse() {
    assertFalse(TextUtils.isPalindrome(null));
  }

  @Test
  void isPalindrome_whenMixedCasePalindrome_returnsTrue() {
    assertTrue(TextUtils.isPalindrome("RaceCar"));
  }

  @Test
  void isPalindrome_whenTextIsNotPalindrome_returnsFalse() {
    assertFalse(TextUtils.isPalindrome("hello"));
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
  void normalizeDisplayName_whenEmpty_returnsEmpty() {
    assertEquals("", TextUtils.normalizeDisplayName(""));
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
  void normalizeDisplayName_matchesAcceptanceExamples() {
    assertEquals("John Smith", TextUtils.normalizeDisplayName("  John   Smith  "));
    assertEquals("John Smith", TextUtils.normalizeDisplayName("John Smith"));
    assertEquals("John", TextUtils.normalizeDisplayName(" John "));
  }

  @Test
  void normalizeDisplayName_whenContentHasNoWhitespace_preservesContent() {
    assertEquals("Ada-Lovelace_42!", TextUtils.normalizeDisplayName("Ada-Lovelace_42!"));
  }
}
