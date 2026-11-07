package fixture.marketplace;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record SalesReportLine(UUID artworkId, Instant soldAt, BigDecimal saleAmount) {
  public SalesReportLine {
    Objects.requireNonNull(artworkId, "artworkId");
    Objects.requireNonNull(soldAt, "soldAt");
    Objects.requireNonNull(saleAmount, "saleAmount");
  }
}