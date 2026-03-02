package car_rental_app.domain.event;

import java.time.Instant;

public class CarMarkedAsUnavailableEvent extends DomainEvent {
    private final String carId;
    private final Instant occurredAt = Instant.now();

    public CarMarkedAsUnavailableEvent(String carId) {
        this.carId = carId;
    }

    public String carId() {
        return carId;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public String eventName() {
        return "CarMarkedAsUnavailableEvent";
    }
}
