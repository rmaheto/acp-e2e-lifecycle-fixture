package fixture.marketplace;

import java.math.BigDecimal;

/** Optional visitor-supplied filters for an artwork search. */
public record ArtworkSearchCriteria(
    String artist,
    String title,
    String medium,
    BigDecimal minimumPrice,
    BigDecimal maximumPrice) {

  /** Returns criteria that do not restrict the search. */
  public static ArtworkSearchCriteria empty() {
    return new ArtworkSearchCriteria(null, null, null, null, null);
  }
}