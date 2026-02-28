package car_rental_app.adapter.in.rest;

import car_rental_app.application.query.GetAvailableCarsQuery;
import car_rental_app.application.query.GetCarByIdQuery;
import car_rental_app.application.service.CarQueryService;
import car_rental_app.domain.model.CarId;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.MDC;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/cars")
public class CarQueryController {
    private static final Logger log = Logger.getLogger(CarQueryController.class.getName());
    private final CarQueryService queryService;

    public CarQueryController(CarQueryService queryService) {
        this.queryService = queryService;
    }

    private String trace() {
        return "trace=" + MDC.get("traceId") + " span=" + MDC.get("spanId");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Optional<?> getById(@PathVariable @NotBlank String id) {
        log.info(() -> "[" + trace() + "] REST: GetCarById id=" + id);
        return queryService.handle(new GetCarByIdQuery(new CarId(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<?> getAvailable() {
        log.info(() -> "[" + trace() + "] REST: GetAvailableCars");
        return queryService.handle(new GetAvailableCarsQuery());
    }
}
