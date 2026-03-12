package car.rental.app.domain.model;

import car.rental.app.domain.event.DomainEvent;
import car.rental.app.domain.event.ReservationCancelledEvent;
import car.rental.app.domain.event.ReservationConfirmedEvent;
import car.rental.app.domain.event.ReservationCreatedEvent;
import car_rental_app.domain.event.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Reservation {

    private final ReservationId id;
    private final CarId carId;
    private final CustomerId customerId;
    private final DateRange dateRange;
    private final Money price;

    private ReservationStatus status;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Reservation(CarId carId, CustomerId customerId, DateRange dateRange, Money price) {
        this.id = ReservationId.generate();
        this.carId = Objects.requireNonNull(carId, "carId cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "customerId cannot be null");
        this.dateRange = Objects.requireNonNull(dateRange, "dateRange cannot be null");
        this.price = Objects.requireNonNull(price, "price cannot be null");
        this.status = ReservationStatus.CREATED;

        domainEvents.add(new ReservationCreatedEvent(
                id.value(),
                carId.value(),
                customerId.value(),
                "ReservationCreatedEvent",
                Instant.now()
        ));
    }

    public ReservationId id() {
        return id;
    }

    public CarId carId() {
        return carId;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public DateRange dateRange() {
        return dateRange;
    }

    public Money price() {
        return price;
    }

    public ReservationStatus status() {
        return status;
    }

    public boolean isConfirmed() {
        return status == ReservationStatus.CONFIRMED;
    }

    public boolean isCancelled() {
        return status == ReservationStatus.CANCELLED;
    }

    public void confirm() {
        if (isCancelled()) {
            throw new IllegalStateException("Cannot confirm a cancelled reservation");
        }
        if (isConfirmed()) {
            throw new IllegalStateException("Reservation already confirmed");
        }

        this.status = ReservationStatus.CONFIRMED;

        domainEvents.add(new ReservationConfirmedEvent(
                id.value(),
                "ReservationConfirmedEvent",
                Instant.now()
        ));
    }

    public void cancel(String reason) {
        Objects.requireNonNull(reason, "Cancellation reason cannot be null");

        if (isCancelled()) {
            throw new IllegalStateException("Reservation already cancelled");
        }

        this.status = ReservationStatus.CANCELLED;

        domainEvents.add(new ReservationCancelledEvent(
                id.value(),
                reason,
                "ReservationCancelledEvent",
                Instant.now()
        ));
    }

    public List<DomainEvent> drainDomainEvents() {
        List<DomainEvent> drained = Collections.unmodifiableList(new ArrayList<>(domainEvents));
        domainEvents.clear();
        return drained;
    }
}
