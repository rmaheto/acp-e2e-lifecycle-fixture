package fixture.marketplace;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** Deterministic sample data for local demonstration; replace with a production catalog provider. */
@Component
public class InMemoryArtworkCatalog implements ArtworkCatalog {
    private final List<Artwork> artworks = List.of(
            new Artwork(UUID.fromString("11111111-1111-4111-8111-111111111111"), "Claude Monet", "Water Lilies", "Oil on canvas", new BigDecimal("850.00")),
            new Artwork(UUID.fromString("22222222-2222-4222-8222-222222222222"), "Mary Cassatt", "The Boating Party", "Oil on canvas", new BigDecimal("1200.00")),
            new Artwork(UUID.fromString("33333333-3333-4333-8333-333333333333"), "Vincent van Gogh", "Almond Blossoms", "Oil on canvas", new BigDecimal("2400.00")),
            new Artwork(UUID.fromString("44444444-4444-4444-8444-444444444444"), "Unknown", "Untitled Study", "Watercolor", new BigDecimal("150.00")));

    @Override
    public List<Artwork> findAll() {
        return artworks;
    }
}
