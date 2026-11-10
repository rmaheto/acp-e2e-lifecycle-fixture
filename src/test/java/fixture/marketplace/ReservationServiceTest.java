package fixture.marketplace;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationServiceTest {
    private static final UUID ARTWORK_ID = UUID.fromString("11111111-1111-4111-8111-111111111111");
    private static final Artwork ARTWORK = new Artwork(ARTWORK_ID, "Claude Monet", "Water Lilies", "Oil on canvas", null);

    @Test
    void reservationExpiresAfterExactly72HoursAndAvailabilityReturns() {
        MutableClock clock = new MutableClock(Instant.parse("2026-01-01T12:00:00Z"));
        ReservationService service = new ReservationService(() -> List.of(ARTWORK), clock);

        Reservation reservation = service.reserve(ARTWORK_ID);
        assertEquals(ReservationStatus.RESERVED, reservation.status());
        assertEquals(clock.instant(), reservation.createdAt());
        assertEquals(clock.instant().plus(Duration.ofHours(72)), reservation.expiresAt());
        assertFalse(service.isAvailable(ARTWORK_ID));

        clock.advance(Duration.ofHours(72).minusNanos(1));
        assertFalse(service.isAvailable(ARTWORK_ID));
        clock.advance(Duration.ofNanos(1));
        assertTrue(service.isAvailable(ARTWORK_ID));
        Reservation next = service.reserve(ARTWORK_ID);
        assertNotNull(next);
        assertFalse(service.isAvailable(ARTWORK_ID));
    }

    @Test
    void scheduledExpirySweepTransitionsAtThe72HourBoundaryAndAllowsAnotherReservation() {
        MutableClock clock = new MutableClock(Instant.parse("2026-01-01T12:00:00Z"));
        ReservationService service = new ReservationService(() -> List.of(ARTWORK), clock);
        Reservation original = service.reserve(ARTWORK_ID);

        clock.advance(Duration.ofHours(72).minusNanos(1));
        assertEquals(0, service.expireDueReservations());
        assertFalse(service.isAvailable(ARTWORK_ID));

        clock.advance(Duration.ofNanos(1));
        assertEquals(1, service.expireDueReservations());
        assertTrue(service.isAvailable(ARTWORK_ID));
        assertEquals(0, service.expireDueReservations());
        Reservation next = service.reserve(ARTWORK_ID);
        assertNotNull(next);
        assertNotNull(original.id());
        assertFalse(service.isAvailable(ARTWORK_ID));
    }

    @Test
    void rejectsUnknownArtwork() {
        ReservationService service = new ReservationService(() -> List.of(ARTWORK), Clock.systemUTC());
        UUID unknownId = UUID.randomUUID();
        assertThrows(ReservationService.UnknownArtworkException.class, () -> service.reserve(unknownId));
        assertFalse(service.isAvailable(unknownId));
    }

    @Test
    void competingAttemptsForOneArtworkAllowOnlyOneReservation() throws Exception {
        ReservationService service = new ReservationService(() -> List.of(ARTWORK), Clock.systemUTC());
        int attempts = 12;
        CountDownLatch ready = new CountDownLatch(attempts);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger conflicted = new AtomicInteger();
        try (var executor = Executors.newFixedThreadPool(attempts)) {
            for (int i = 0; i < attempts; i++) {
                executor.submit(() -> {
                    ready.countDown();
                    try {
                        start.await();
                        service.reserve(ARTWORK_ID);
                        succeeded.incrementAndGet();
                    } catch (ReservationService.ReservationConflictException exception) {
                        conflicted.incrementAndGet();
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            ready.await();
            start.countDown();
        }
        assertEquals(1, succeeded.get());
        assertEquals(attempts - 1, conflicted.get());
        assertFalse(service.isAvailable(ARTWORK_ID));
    }

    private static final class MutableClock extends Clock {
        private Instant now;

        private MutableClock(Instant now) { this.now = now; }
        @Override public ZoneOffset getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(java.time.ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
        private void advance(Duration duration) { now = now.plus(duration); }
    }
}
