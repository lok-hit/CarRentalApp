package car_rental_app.adapter.in.web.controller;

import car_rental_app.adapter.in.web.dto.ReservationDetailsResponse;
import car_rental_app.application.query.ReservationQueryService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@Validated
public class ReservationQueryController {

    private final ReservationQueryService queryService;

    public ReservationQueryController(ReservationQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDetailsResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(queryService.getReservationDetails(id));
    }

    @GetMapping("/customer/{customerId}")
    public List<ReservationDetailsResponse> listByCustomer(@PathVariable @NotBlank String customerId,
                                                           @RequestParam(defaultValue = "0")
                                                           @Min(0) int page,
                                                           @RequestParam(defaultValue = "20")
                                                               @Min(1) int size) {
        int cappedSize = Math.min(size, 100);
        return queryService.listReservationsForCustomer(customerId, page, cappedSize);
    }
}

