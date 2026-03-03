package car_rental_app.adapter.in.web.controller;

import car_rental_app.adapter.in.web.dto.ReservationDetailsResponse;
import car_rental_app.application.query.ReservationQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationQueryController {

    private final ReservationQueryService queryService;

    public ReservationQueryController(ReservationQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDetailsResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(queryService.getReservationDetails(id));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDetailsResponse>> listByCustomer(
            @RequestParam(name = "customerId", required = false) String customerId
    ) {
        if (customerId == null || customerId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(queryService.listReservationsForCustomer(customerId));
    }
}
