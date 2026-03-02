package car_rental_app.application.service;

import car_rental_app.application.command.ChangeCarPriceCommand;
import car_rental_app.application.command.CreateCarCommand;
import car_rental_app.application.command.MarkCarAsAvailableCommand;
import car_rental_app.application.command.MarkCarAsUnavailableCommand;
import car_rental_app.application.service.exception.CarReservationRejectedException;
import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarCategory;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.model.Price;
import car_rental_app.application.port.CarCommandPort;
import car_rental_app.domain.port.CarRepository;
import car_rental_app.domain.port.EventPublisher;
import car_rental_app.domain.port.ReservationPolicy;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class CarApplicationService implements CarCommandPort {

    private static final Logger log = Logger.getLogger(CarApplicationService.class.getName());
    private final CarRepository repository;
    private final EventPublisher eventPublisher;
    private final ReservationPolicy reservationPolicy;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates a CarApplicationService with the required dependencies.
     *
     * @param repository       repository for loading and saving Car aggregates
     * @param eventPublisher   publisher for domain events
     * @param reservationPolicy policy enforcing reservation constraints
     */
    @Autowired
    public CarApplicationService(CarRepository repository, EventPublisher eventPublisher, ReservationPolicy reservationPolicy) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.reservationPolicy = reservationPolicy;
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
                .orElseThrow(() -> new IllegalArgumentException("Car not found: " + cmd.id()));
        car.changePrice(new Price(cmd.newPrice()));
        repository.save(car);
        publishEvents(car);
    }

    /**
     * Marks the specified car as unavailable after validating reservation constraints.
     *
     * Loads the car identified by the command, enforces the reservation policy, persists the state change,
     * and publishes any resulting domain events.
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
            return new IllegalArgumentException("Car not found: " + cmd.id());
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
     * Processes a MarkCarAsAvailableCommand by marking the referenced car available, persisting the change, and publishing any domain events.
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
                    return new IllegalArgumentException("Car not found: " + cmd.id());
                });
        car.markAsAvailable();
        repository.save(car);
        publishEvents(car);
        log.info(() -> "[" + trace() + "] Car marked AVAILABLE id=" + cmd.id());
    }

    /**
     * Publishes and clears all domain events emitted by the given car aggregate to the application event publisher.
     *
     * @param car the Car aggregate whose drained domain events will be published
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