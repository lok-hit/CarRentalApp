package car.rental.app.adapter.in.rest;

import car.rental.app.application.command.ChangeCarPriceCommand;
import car.rental.app.application.command.CreateCarCommand;
import car.rental.app.application.command.MarkCarAsAvailableCommand;
import car.rental.app.application.command.MarkCarAsUnavailableCommand;
import car.rental.app.domain.model.CarId;
import car.rental.app.application.port.CarCommandPort;
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

    /**
     * Creates a new car from the given creation command.
     *
     * @param cmd the command containing the new car's identifier and attributes
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void create(@Valid @RequestBody CreateCarCommand cmd) {
        log.info(() -> "[" + trace() + "] REST: CreateCar id=" + cmd.id());
        commandPort.handle(cmd);
    }

    /**
     * Validates that the path car id matches the id in the request body and forwards a change-price command.
     *
     * @param id  the car identifier from the request path
     * @param cmd the change-price command containing the target car id and new price
     * @throws IllegalArgumentException if the path `id` does not equal `cmd.id()`
     */
    @PutMapping("/{id}/price")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void changePrice(@PathVariable @NotBlank String id, @RequestBody ChangeCarPriceCommand cmd) {
        log.info(() -> "[" + trace() + "] REST: ChangeCarPrice id=" + id);
        if (!id.equals(cmd.id().value())) {
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

    /**
     * Marks the car identified by the given id as unavailable.
     *
     * @param id the car identifier (must not be blank)
     */
    @PostMapping("/{id}/unavailable")
    @PreAuthorize("hasRole('OPS')")
    public void markUnavailable(@NotBlank @PathVariable String id) {
        log.info(() -> "[" + trace() + "] REST: MarkUnavailable id=" + id);
        commandPort.handle(new MarkCarAsUnavailableCommand(new CarId(id).value()));
    }

    /**
     * Builds a single-line log context string containing the MDC trace and span identifiers.
     *
     * @return the formatted string "trace=&lt;traceId&gt; span=&lt;spanId&gt;" where &lt;traceId&gt; and &lt;spanId&gt; are the values read from MDC keys "traceId" and "spanId"
     */
    private String trace() {
        return "trace=" + MDC.get("traceId") + " span=" + MDC.get("spanId");
    }
}

