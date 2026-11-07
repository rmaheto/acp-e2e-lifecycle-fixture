package fixture.marketplace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MarketplaceRecordsTest {

  @Test
  void recordsExposeMarketplaceSearchAndAccountFields() {
    var artwork = new Artwork(
        UUID.randomUUID(), "Ada Artist", "Blue Study", "Oil", new BigDecimal("1250.00"));
    var account = new GalleryAccount(UUID.randomUUID(), "gallery@example.test", "hashed-credential");

    assertEquals("Ada Artist", artwork.artist());
    assertEquals("Blue Study", artwork.title());
    assertEquals("Oil", artwork.medium());
    assertEquals(new BigDecimal("1250.00"), artwork.price());
    assertEquals("gallery@example.test", account.emailAddress());
    assertEquals("hashed-credential", account.passwordHash());
  }

  @Test
  void reservationExpires72HoursAfterCreationAndIncludesStatus() {
    Instant createdAt = Instant.parse("2026-01-01T12:00:00Z");
    var reservation = new Reservation(
        UUID.randomUUID(), UUID.randomUUID(), ReservationStatus.RESERVED, createdAt);

    assertEquals(ReservationStatus.RESERVED, reservation.status());
    assertEquals(Instant.parse("2026-01-04T12:00:00Z"), reservation.expiresAt());
  }

  @Test
  void salesReportRepresentsPeriodAndSaleLines() {
    var line = new SalesReportLine(
        UUID.randomUUID(), Instant.parse("2026-03-15T10:30:00Z"), new BigDecimal("900.00"));
    var report = new SalesReport(
        UUID.randomUUID(),
        UUID.randomUUID(),
        LocalDate.parse("2026-03-01"),
        LocalDate.parse("2026-03-31"),
        List.of(line));

    assertEquals(LocalDate.parse("2026-03-01"), report.periodStart());
    assertEquals(LocalDate.parse("2026-03-31"), report.periodEnd());
    assertEquals(List.of(line), report.lines());
    assertEquals(new BigDecimal("900.00"), report.lines().get(0).saleAmount());
    assertTrue(report.lines().get(0).soldAt().isAfter(Instant.parse("2026-03-01T00:00:00Z")));
  }
}