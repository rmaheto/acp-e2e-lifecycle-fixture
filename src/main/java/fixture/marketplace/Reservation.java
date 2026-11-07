package fixture.marketplace;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Reservation(UUID id, UUID artworkId, ReservationStatus status, Instant createdAt) {
  public static final Duration EXPIRY_DURATION = Duration.ofHours(72);

  public Reservation {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(artworkId, "artworkId");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(createdAt, "createdAt");
  }

  public Instant expiresAt() {
    return createdAt.plus(EXPIRY_DURATION);
  }
}