package car_rental_app.adapter.in.rest;

import car_rental_app.application.command.ChangeCarPriceCommand;
import car_rental_app.application.command.CreateCarCommand;
import car_rental_app.application.command.MarkCarAsAvailableCommand;
import car_rental_app.application.command.MarkCarAsUnavailableCommand;
import car_rental_app.domain.model.CarId;
import car_rental_app.application.port.CarCommandPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/cars")
public class CarCommandController {

    private static final Logger log = Logger.getLogger(CarCommandController.class.getName());
    private final CarCommandPort commandPort;

    @Autowired
    public CarCommandController(CarCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void create(@Valid @RequestBody CreateCarCommand cmd) {
        log.info(() -> "[" + trace() + "] REST: CreateCar id=" + cmd.id());
        commandPort.handle(cmd);
    }

    @PutMapping("/{id}/price")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void changePrice(@PathVariable @NotBlank String id, @RequestBody ChangeCarPriceCommand cmd) {
        log.info(() -> "[" + trace() + "] REST: ChangeCarPrice id=" + id);
        if (!id.equals(cmd.id())) {
            throw new IllegalArgumentException("Path id does not match body id");
        }
        commandPort.handle(cmd);
    }

    @PostMapping("/{id}/available")
    @PreAuthorize("hasRole('OPS')")
    public void markAvailable(@NotBlank @PathVariable String id) {
        log.info(() -> "[" + trace() + "] REST: MarkAvailable id=" + id);
        commandPort.handle(new MarkCarAsAvailableCommand(new CarId(id)));
    }

    @PostMapping("/{id}/unavailable")
    @PreAuthorize("hasRole('OPS')")
    public void markUnavailable(@NotBlank @PathVariable String id) {
        log.info(() -> "[" + trace() + "] REST: MarkUnavailable id=" + id);
        commandPort.handle(new MarkCarAsUnavailableCommand(new CarId(id).value()));
    }

    private String trace() {
        return "trace=" + MDC.get("traceId") + " span=" + MDC.get("spanId");
    }
}

