package car_rental_app.application.service;

import car_rental_app.application.command.ChangeCarPriceCommand;
import car_rental_app.application.command.CreateCarCommand;
import car_rental_app.application.command.MarkCarAsAvailableCommand;
import car_rental_app.application.command.MarkCarAsUnavailableCommand;
import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarCategory;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.model.Price;
import car_rental_app.domain.port.CarCommandPort;
import car_rental_app.domain.port.CarRepository;
import car_rental_app.domain.port.EventPublisher;
import car_rental_app.domain.port.ReservationPolicy;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional
    public void handle(MarkCarAsUnavailableCommand cmd) {

        log.info(() -> "[" + trace() + "] MarkCarAsUnavailableCommand id=" + cmd.id());
        Car car = repository.findById(new CarId(cmd.id().value())).orElseThrow(() -> {
            log.warning(() -> "[" + trace() + "] Car not found id=" + cmd.id());
            return new IllegalArgumentException("Car not found: " + cmd.id());
        });
        if (!reservationPolicy.canBeReserved(car)) {
            log.info(() -> "[" + trace() + "] Reservation policy rejected id=" + cmd.id());
            return;
        }
        car.markAsUnavailable();
        repository.save(car);
        publishEvents(car);
        log.info(() -> "[" + trace() + "] Car marked UNAVAILABLE id=" + cmd.id());
    }

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

    private void publishEvents(Car car) {
        car.getDomainEvents().forEach(event -> eventPublisher.publish("car-events", event));
    }

    private String trace() {

        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        return "trace=" + (traceId != null ? traceId : "none") + " span=" +
                (spanId != null ? spanId : "none");
    }
}