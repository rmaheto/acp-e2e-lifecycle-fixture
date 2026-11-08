package fixture.marketplace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ArtworkSearchServiceTest {
  private final ArtworkSearchService service = new ArtworkSearchService();

  @Test
  void matchesEachTextFieldPartiallyAndWithoutCaseSensitivity() {
    Artwork artwork = artwork("Ada Artist", "Blue Study", "Oil", "125.00");

    assertEquals(List.of(artwork), service.search(List.of(artwork),
        new ArtworkSearchCriteria("ADA", null, null, null, null)));
    assertEquals(List.of(artwork), service.search(List.of(artwork),
        new ArtworkSearchCriteria(null, "study", null, null, null)));
    assertEquals(List.of(artwork), service.search(List.of(artwork),
        new ArtworkSearchCriteria(null, null, "OI", null, null)));
  }

  @Test
  void combinesCriteriaAndAppliesInclusivePriceBounds() {
    Artwork lower = artwork("Ada Artist", "Blue Study", "Oil", "100.00");
    Artwork upper = artwork("Ada Artist", "Red Study", "Oil", "200.00");
    Artwork outside = artwork("Ada Artist", "Green Study", "Oil", "200.01");
    var criteria = new ArtworkSearchCriteria(
        "ada", "study", "oil", new BigDecimal("100"), new BigDecimal("200"));

    assertEquals(List.of(lower, upper), service.search(List.of(lower, upper, outside), criteria));
  }

  @Test
  void ignoresBlankFieldsAndMissingAttributesWhenNotRequested() {
    Artwork missing = new Artwork(UUID.randomUUID(), null, null, null, null);
    Artwork complete = artwork("Ada Artist", "Study", "Oil", "50");

    assertEquals(List.of(missing, complete), service.search(List.of(missing, complete),
        new ArtworkSearchCriteria("  ", null, "", null, null)));
    assertEquals(List.of(complete), service.search(List.of(missing, complete),
        new ArtworkSearchCriteria("Ada", null, null, null, null)));
    assertEquals(List.of(complete), service.search(List.of(missing, complete),
        new ArtworkSearchCriteria(null, null, null, BigDecimal.ZERO, null)));
  }

  @Test
  void omittedBoundsDoNotRestrictAndEmptyOrNullInputsAreSafe() {
    Artwork inexpensive = artwork("A", "One", "Ink", "1");
    Artwork expensive = artwork("B", "Two", "Canvas", "9000");

    assertEquals(List.of(inexpensive, expensive),
        service.search(List.of(inexpensive, expensive), ArtworkSearchCriteria.empty()));
    assertTrue(service.search(List.of(inexpensive),
        new ArtworkSearchCriteria("no match", null, null, null, null)).isEmpty());
    assertTrue(service.search(List.of(), ArtworkSearchCriteria.empty()).isEmpty());
    assertTrue(service.search(null, ArtworkSearchCriteria.empty()).isEmpty());
    assertEquals(List.of(inexpensive), service.search(List.of(inexpensive), null));
  }

  private static Artwork artwork(String artist, String title, String medium, String price) {
    return new Artwork(UUID.randomUUID(), artist, title, medium,
        price == null ? null : new BigDecimal(price));
  }
}
