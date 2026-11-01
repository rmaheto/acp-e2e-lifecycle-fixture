package fixture;

/** Small text helpers for the disposable E2E lifecycle fixture. */
public final class TextUtils {

  private TextUtils() {}

  /** Returns the text upper-cased, or an empty string for null. */
  public static String shout(String text) {
    return text == null ? "" : text.toUpperCase();
  }
}
