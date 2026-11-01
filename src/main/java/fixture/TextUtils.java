package fixture;

/** Small text helpers for the disposable E2E lifecycle fixture. */
public final class TextUtils {

  private TextUtils() {}

  /** Returns the text upper-cased, or an empty string for null. */
  public static String shout(String text) {
    return text == null ? "" : text.toUpperCase();
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
