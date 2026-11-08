package fixture.marketplace;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArtworkSearchServiceTest {
    private final ArtworkSearchService service = new ArtworkSearchService(() -> List.of(
            new Artwork(java.util.UUID.randomUUID(), "Claude Monet", "Water Lilies", "Oil on canvas", new BigDecimal("850.00")),
            new Artwork(java.util.UUID.randomUUID(), "Mary Cassatt", "The Boating Party", "Oil on canvas", new BigDecimal("1200.00")),
            new Artwork(java.util.UUID.randomUUID(), "Unknown", "Untitled", null, null)));

    @Test
    void combinesCaseInsensitivePartialTextAndInclusivePriceFilters() {
        assertEquals(1, service.search("mon", "lil", "CANVAS", new BigDecimal("850"), new BigDecimal("850")).size());
        assertEquals("Claude Monet", service.search("", null, null, null, null).get(0).artist());
        assertEquals(1, service.search(null, null, null, new BigDecimal("1200"), new BigDecimal("1200")).size());
    }

    @Test
    void ignoresBlankFiltersAndHandlesMissingAttributesSafely() {
        assertEquals(3, service.search("  ", "", null, null, null).size());
        assertEquals(0, service.search(null, null, "canvas", null, null).size());
        assertEquals(0, service.search(null, null, null, new BigDecimal("0"), null).stream()
                .filter(artwork -> artwork.price() == null).count());
    }

    @Test
    void rejectsInvalidPriceRanges() {
        assertThrows(IllegalArgumentException.class, () -> service.search(null, null, null, new BigDecimal("-1"), null));
        assertThrows(IllegalArgumentException.class, () -> service.search(null, null, null, new BigDecimal("11"), new BigDecimal("10")));
    }
}
