package fixture.marketplace;

import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequest request) {
        if (request == null || request.artworkId() == null) {
            throw new IllegalArgumentException("An artworkId is required.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.reserve(request.artworkId()));
    }

    @ExceptionHandler(ReservationService.ReservationConflictException.class)
    public ResponseEntity<Map<String, String>> conflict(ReservationService.ReservationConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

    public record ReservationRequest(UUID artworkId) { }
}
