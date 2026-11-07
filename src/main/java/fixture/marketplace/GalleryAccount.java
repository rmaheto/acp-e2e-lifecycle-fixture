package fixture.marketplace;

import java.util.UUID;

public record GalleryAccount(UUID id, String emailAddress, String passwordHash) {
}