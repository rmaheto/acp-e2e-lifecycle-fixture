package fixture.marketplace;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record SalesReport(
    UUID id,
    UUID galleryAccountId,
    LocalDate periodStart,
    LocalDate periodEnd,
    List<SalesReportLine> lines) {

  public SalesReport {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(galleryAccountId, "galleryAccountId");
    Objects.requireNonNull(periodStart, "periodStart");
    Objects.requireNonNull(periodEnd, "periodEnd");
    lines = List.copyOf(Objects.requireNonNull(lines, "lines"));
  }
}