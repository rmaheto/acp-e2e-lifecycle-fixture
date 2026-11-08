package fixture.marketplace;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class ArtworkSearchService {
    private final ArtworkCatalog catalog;

    public ArtworkSearchService(ArtworkCatalog catalog) {
        this.catalog = catalog;
    }

    public List<Artwork> search(String artist, String title, String medium, BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && minPrice.signum() < 0 || maxPrice != null && maxPrice.signum() < 0) {
            throw new IllegalArgumentException("Price bounds must be non-negative.");
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price must not exceed maximum price.");
        }
        return catalog.findAll().stream()
                .filter(artwork -> matches(artwork.artist(), artist))
                .filter(artwork -> matches(artwork.title(), title))
                .filter(artwork -> matches(artwork.medium(), medium))
                .filter(artwork -> matchesPrice(artwork.price(), minPrice, maxPrice))
                .toList();
    }

    private boolean matches(String value, String query) {
        if (query == null || query.isBlank()) return true;
        return value != null && value.toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT));
    }

    private boolean matchesPrice(BigDecimal price, BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null && maxPrice == null) return true;
        if (price == null) return false;
        return (minPrice == null || price.compareTo(minPrice) >= 0)
                && (maxPrice == null || price.compareTo(maxPrice) <= 0);
    }
}
