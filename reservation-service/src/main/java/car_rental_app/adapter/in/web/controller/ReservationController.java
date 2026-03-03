package car_rental_app.adapter.in.web.controller;

import car_rental_app.adapter.in.web.dto.*;
import car_rental_app.application.service.ReservationApplicationService;
import car_rental_app.domain.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

@RestController
@RequestMapping("/reservations")
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {

    private final ReservationApplicationService service;

    public ReservationController(ReservationApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new reservation")
    public ReservationResponse create(@RequestBody CreateReservationRequest request,
                                      @AuthenticationPrincipal Jwt jwt) {

        ReservationId id = service.createReservation(
                new CarId(request.carId()),
                new CustomerId(request.customerId()),
                new DateRange(request.startDate(), request.endDate()),
                new Money(request.priceAmount(), Currency.getInstance(request.priceCurrency()))
        );

        return new ReservationResponse(id.value(), "CREATED");
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm reservation")
    public ReservationResponse confirm(@PathVariable String id) {
        service.confirmReservation(id);
        return new ReservationResponse(id, "CONFIRMED");
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel reservation")
    public ReservationResponse cancel(@PathVariable String id,
                                      @RequestBody CancelReservationRequest request) {
        service.cancelReservation(id, request.reason());
        return new ReservationResponse(id, "CANCELLED");
    }
}
