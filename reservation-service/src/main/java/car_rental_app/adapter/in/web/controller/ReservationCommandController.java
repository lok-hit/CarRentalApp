package car_rental_app.adapter.in.web.controller;

import car_rental_app.application.command.*;
import car_rental_app.application.handler.ReservationCommandHandler;
import car_rental_app.domain.model.ReservationId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/reservations")
public class ReservationCommandController {

    private final ReservationCommandHandler handler;

    public ReservationCommandController(ReservationCommandHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateReservationCommand cmd) {
        ReservationId id = handler.handle(cmd);
        return ResponseEntity
                .created(URI.create("/reservations/" + id.value()))
                .build();
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable String id) {
        handler.handle(new ConfirmReservationCommand(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id, @RequestBody CancelReservationCommand cmd) {
        handler.handle(new CancelReservationCommand(id, cmd.reason()));
        return ResponseEntity.noContent().build();
    }
}
