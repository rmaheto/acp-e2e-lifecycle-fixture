package fixture.marketplace;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkSearchController {
    private final ArtworkSearchService searchService;
    private final ReservationService reservationService;

    public ArtworkSearchController(ArtworkSearchService searchService, ReservationService reservationService) {
        this.searchService = searchService;
        this.reservationService = reservationService;
    }

    @GetMapping("/search")
    public SearchResponse search(
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String medium,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice) {
        List<SearchResult> results = searchService.search(artist, title, medium, parsePrice(minPrice), parsePrice(maxPrice))
                .stream()
                .map(artwork -> new SearchResult(artwork.id(), artwork.artist(), artwork.title(), artwork.medium(),
                        artwork.price(), reservationService.isAvailable(artwork.id())))
                .toList();
        return new SearchResponse(results, results.size());
    }

    private BigDecimal parsePrice(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Prices must be valid decimal numbers.");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

    public record SearchResponse(List<SearchResult> results, int count) { }

    public record SearchResult(java.util.UUID id, String artist, String title, String medium, BigDecimal price,
            boolean available) { }
}
