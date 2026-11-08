package fixture.marketplace;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/** Filters supplied artwork records without owning catalog retrieval or persistence. */
public class ArtworkSearchService {

  /**
   * Returns artworks matching every supplied criterion. Text matching is case-insensitive and
   * partial; price bounds are inclusive. Missing artwork attributes never match supplied text,
   * and artworks without a price are excluded when a price bound is active.
   */
  public List<Artwork> search(
      Collection<Artwork> artworks, ArtworkSearchCriteria criteria) {
    if (artworks == null || artworks.isEmpty()) {
      return List.of();
    }

    ArtworkSearchCriteria filters = criteria == null ? ArtworkSearchCriteria.empty() : criteria;
    Stream<Artwork> candidates = artworks.stream().filter(Objects::nonNull);
    return candidates
        .filter(artwork -> matchesText(artwork.artist(), filters.artist()))
        .filter(artwork -> matchesText(artwork.title(), filters.title()))
        .filter(artwork -> matchesText(artwork.medium(), filters.medium()))
        .filter(artwork -> matchesPrice(artwork.price(), filters.minimumPrice(), filters.maximumPrice()))
        .toList();
  }

  private static boolean matchesText(String attribute, String query) {
    if (query == null || query.isBlank()) {
      return true;
    }
    return attribute != null
        && attribute.toLowerCase(java.util.Locale.ROOT)
            .contains(query.trim().toLowerCase(java.util.Locale.ROOT));
  }

  private static boolean matchesPrice(
      BigDecimal price, BigDecimal minimumPrice, BigDecimal maximumPrice) {
    if (minimumPrice == null && maximumPrice == null) {
      return true;
    }
    if (price == null) {
      return false;
    }
    return (minimumPrice == null || price.compareTo(minimumPrice) >= 0)
        && (maximumPrice == null || price.compareTo(maximumPrice) <= 0);
  }
}