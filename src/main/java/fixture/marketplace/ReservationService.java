package fixture.marketplace;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/** In-memory reservation lifecycle with per-artwork serialization of competing requests. */
@Service
public class ReservationService {
    private final ArtworkCatalog catalog;
    private final Clock clock;
    private final ConcurrentMap<UUID, Reservation> reservations = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, Object> artworkLocks = new ConcurrentHashMap<>();

    @Autowired
    public ReservationService(ArtworkCatalog catalog) {
        this(catalog, Clock.systemUTC());
    }

    public ReservationService(ArtworkCatalog catalog, Clock clock) {
        this.catalog = catalog;
        this.clock = clock;
    }

    public Reservation reserve(UUID artworkId) {
        if (artworkId == null || catalog.findAll().stream().noneMatch(artwork -> artwork.id().equals(artworkId))) {
            throw new UnknownArtworkException("Unknown artwork.");
        }
        Object lock = artworkLocks.computeIfAbsent(artworkId, ignored -> new Object());
        synchronized (lock) {
            Instant now = clock.instant();
            Reservation existing = reservations.get(artworkId);
            if (isActive(existing, now)) {
                throw new ReservationConflictException("This artwork already has an active reservation.");
            }
            Reservation reservation = new Reservation(UUID.randomUUID(), artworkId, ReservationStatus.RESERVED, now);
            reservations.put(artworkId, reservation);
            return reservation;
        }
    }

    public boolean isAvailable(UUID artworkId) {
        if (artworkId == null || catalog.findAll().stream().noneMatch(artwork -> artwork.id().equals(artworkId))) {
            return false;
        }
        return !isActive(reservations.get(artworkId), clock.instant());
    }

    /** Periodically expire reservations whose 72-hour period has ended. */
    @Scheduled(fixedDelay = 60_000)
    public void expireReservationsScheduled() {
        expireDueReservations();
    }

    /** Expires due reservations and returns the number of state transitions performed. */
    public int expireDueReservations() {
        Instant now = clock.instant();
        int[] expired = {0};
        reservations.forEach((artworkId, reservation) -> {
            Object lock = artworkLocks.computeIfAbsent(artworkId, ignored -> new Object());
            synchronized (lock) {
                Reservation current = reservations.get(artworkId);
                if (current != null
                        && current.status() == ReservationStatus.RESERVED
                        && !now.isBefore(current.expiresAt())) {
                    reservations.put(artworkId, new Reservation(
                            current.id(), current.artworkId(), ReservationStatus.EXPIRED, current.createdAt()));
                    expired[0]++;
                }
            }
        });
        return expired[0];
    }

    private boolean isActive(Reservation reservation, Instant now) {
        return reservation != null
                && reservation.status() == ReservationStatus.RESERVED
                && now.isBefore(reservation.expiresAt());
    }

    public static class UnknownArtworkException extends IllegalArgumentException {
        public UnknownArtworkException(String message) { super(message); }
    }

    public static class ReservationConflictException extends RuntimeException {
        public ReservationConflictException(String message) { super(message); }
    }
}
