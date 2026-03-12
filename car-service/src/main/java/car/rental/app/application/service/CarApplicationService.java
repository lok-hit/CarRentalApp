package car.rental.app.application.service;

import car.rental.app.application.command.ChangeCarPriceCommand;
import car.rental.app.application.command.CreateCarCommand;
import car.rental.app.application.command.MarkCarAsAvailableCommand;
import car.rental.app.application.command.MarkCarAsUnavailableCommand;
import car.rental.app.application.port.CarCommandPort;
import car.rental.app.application.service.exception.CarReservationRejectedException;
import car.rental.app.domain.model.Car;
import car.rental.app.domain.model.CarCategory;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.model.Price;
import car.rental.app.domain.port.CarRepository;
import car.rental.app.domain.port.ReservationPolicy;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class CarApplicationService implements CarCommandPort {

    private static final String CAR_NOT_FOUND_MESSAGE = "Car not found: ";
    private static final Logger log = Logger.getLogger(CarApplicationService.class.getName());
    private final CarRepository repository;
    private final ReservationPolicy reservationPolicy;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates a CarApplicationService with the required dependencies.
     *
     * @param repository       repository for loading and saving Car aggregates
     * @param eventPublisher   publisher for car_rental_app.domain events
     * @param reservationPolicy policy enforcing reservation constraints
     */
    @Autowired
    public CarApplicationService(CarRepository repository, ReservationPolicy reservationPolicy, ApplicationEventPublisher applicationEventPublisher) {
        this.repository = repository;
        this.reservationPolicy = reservationPolicy;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void handle(CreateCarCommand cmd) {

        log.info(() -> "[" + trace() + "] CreateCarCommand id=" + cmd.id());
        Car car = new Car(new CarId(cmd.id().value()), CarCategory.valueOf(cmd.category().name()),
                new Price(cmd.price()));
        repository.save(car);
        publishEvents(car);
    }

    @Override
    @Transactional
    public void handle(ChangeCarPriceCommand cmd) {

        log.info(() -> "[" + trace() + "] ChangeCarPriceCommand id=" + cmd.id());
        Car car = repository.findById(new CarId(cmd.id().value()))
                .orElseThrow(() -> new IllegalArgumentException(CAR_NOT_FOUND_MESSAGE + cmd.id()));
        car.changePrice(new Price(cmd.newPrice()));
        repository.save(car);
        publishEvents(car);
    }

    /**
     * Marks the specified car as unavailable after validating reservation constraints.
     *
     * Loads the car identified by the command, enforces the reservation policy, persists the state change,
     * and publishes any resulting car_rental_app.domain events.
     *
     * @param cmd the command containing the id of the car to mark unavailable
     * @throws IllegalArgumentException if no car exists with the provided id
     * @throws CarReservationRejectedException if the reservation policy prevents marking the car unavailable
     */
    @Override
    @Transactional
    public void handle(MarkCarAsUnavailableCommand cmd) {

        log.info(() -> "[" + trace() + "] MarkCarAsUnavailableCommand id=" + cmd.id());
        Car car = repository.findById(new CarId(cmd.id())).orElseThrow(() -> {
            log.warning(() -> "[" + trace() + "] Car not found id=" + cmd.id());
            return new IllegalArgumentException(CAR_NOT_FOUND_MESSAGE + cmd.id());
        });
        if (!reservationPolicy.canBeReserved(car)) {
            throw new CarReservationRejectedException("[" + trace() + "] Reservation rejected for car=" + cmd.id());
        }
        car.markAsUnavailable();
        repository.save(car);
        publishEvents(car);
        log.info(() -> "[" + trace() + "] Car marked UNAVAILABLE id=" + cmd.id());
    }


    /**
     * Processes a MarkCarAsAvailableCommand by marking the referenced car available, persisting the change, and publishing any car_rental_app.domain events.
     *
     * @param cmd the command containing the identifier of the car to mark as available
     * @throws IllegalArgumentException if no car with the given id exists
     */
    @Override
    @Transactional
    public void handle(MarkCarAsAvailableCommand cmd) {

        log.info(() -> "[" + trace() + "] MarkCarAsAvailableCommand id=" + cmd.id());
        Car car = repository.findById(new CarId(cmd.id().value()))
                .orElseThrow(() -> {
                    log.warning(() -> "[" + trace() + "] Car not found id=" + cmd.id());
                    return new IllegalArgumentException(CAR_NOT_FOUND_MESSAGE + cmd.id());
                });
        car.markAsAvailable();
        repository.save(car);
        publishEvents(car);
        log.info(() -> "[" + trace() + "] Car marked AVAILABLE id=" + cmd.id());
    }

    /**
     * Publishes and clears all car_rental_app.domain events emitted by the given car aggregate to the application event publisher.
     *
     * @param car the Car aggregate whose drained car_rental_app.domain events will be published
     */
    private void publishEvents(Car car) {
        car.drainDomainEvents().forEach(applicationEventPublisher::publishEvent);
    }

    /**
     * Format MDC trace and span identifiers into a single string for logging.
     *
     * @return a string in the form "trace=<traceId|none> span=<spanId|none>" where missing identifiers are replaced with "none"
     */
    private String trace() {

        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        return "trace=" + (traceId != null ? traceId : "none") + " span=" +
                (spanId != null ? spanId : "none");
    }
}