package fixture.marketplace;

import java.math.BigDecimal;
import java.util.UUID;

public record Artwork(UUID id, String artist, String title, String medium, BigDecimal price) {
}