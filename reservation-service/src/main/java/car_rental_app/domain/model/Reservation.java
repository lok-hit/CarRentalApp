package car_rental_app.domain.model;

import car_rental_app.domain.event.ReservationCancelledEvent;
import car_rental_app.domain.event.ReservationConfirmedEvent;
import car_rental_app.domain.event.ReservationCreatedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reservation {

    private final ReservationId reservationId;
    private final CarId carId;
    private final CustomerId customerId;
    private final DateRange dateRange;
    private final Money price;
    private ReservationStatus status;
    private final Instant createdAt;

    private final List<Object> domainEvents = new ArrayList<>();

    public Reservation(CarId carId, CustomerId customerId, DateRange dateRange, Money price) {
        this.reservationId = ReservationId.of(java.util.UUID.randomUUID().toString());
        this.carId = Objects.requireNonNull(carId);
        this.customerId = Objects.requireNonNull(customerId);
        this.dateRange = Objects.requireNonNull(dateRange);
        this.price = Objects.requireNonNull(price);
        this.status = ReservationStatus.PENDING;
        this.createdAt = Instant.now();

        domainEvents.add(new ReservationCreatedEvent(
                reservationId.value(),
                carId.value(),
                customerId.value(),
                price.amount().toString()
        ));
    }

    public void confirm() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Cannot confirm reservation in state: " + status);
        }
        status = ReservationStatus.CONFIRMED;
        domainEvents.add(new ReservationConfirmedEvent(reservationId.value()));
    }

    public void cancel(String reason) {
        if (status == ReservationStatus.CANCELLED) {
            return;
        }
        status = ReservationStatus.CANCELLED;
        domainEvents.add(new ReservationCancelledEvent(reservationId.value(), reason));
    }

    public List<Object> drainEvents() {
        List<Object> copy = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public ReservationId id() {
        return reservationId;
    }

    public ReservationStatus status() {
        return status;
    }
}
