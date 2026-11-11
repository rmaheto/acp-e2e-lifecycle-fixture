package fixture;

/** Small text helpers for the disposable E2E lifecycle fixture. */
public final class TextUtils {

  private TextUtils() {}

  /** Returns the text upper-cased, or an empty string for null. */
  public static String shout(String text) {
    return text == null ? "" : text.toUpperCase();
  }

  /** Returns whether the value is null or has zero length. */
  public static boolean isNullOrEmpty(String value) {
    return value == null || value.length() == 0;
  }

  /** Returns whether the value is null, empty, or contains only whitespace. */
  public static boolean isBlank(String value) {
    if (value == null) {
      return true;
    }

    for (int i = 0; i < value.length(); i++) {
      if (!Character.isWhitespace(value.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /** Returns whether the value contains non-whitespace text. */
  public static boolean isNotBlank(String value) {
    return !isBlank(value);
  }

  /** Returns whether the value ends with the specified suffix. */
  public static boolean endsWith(String value, String suffix) {
    return value != null && suffix != null && value.endsWith(suffix);
  }

  /** Returns whether the value starts with the specified prefix. */
  public static boolean startsWith(String value, String prefix) {
    return value != null && prefix != null && value.startsWith(prefix);
  }

  /** Returns a display name with whitespace trimmed and collapsed to single spaces. */
  public static String normalizeDisplayName(String name) {
    if (name == null) {
      return "";
    }

    StringBuilder normalized = new StringBuilder();
    boolean pendingSpace = false;
    for (int i = 0; i < name.length(); i++) {
      char character = name.charAt(i);
      if (Character.isWhitespace(character)) {
        if (normalized.length() > 0) {
          pendingSpace = true;
        }
      } else {
        if (pendingSpace) {
          normalized.append(' ');
          pendingSpace = false;
        }
        normalized.append(character);
      }
    }
    return normalized.toString();
  }
}
