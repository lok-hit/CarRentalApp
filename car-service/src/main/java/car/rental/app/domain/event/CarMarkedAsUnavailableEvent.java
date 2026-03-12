package car.rental.app.domain.event;

import java.time.Instant;

public class CarMarkedAsUnavailableEvent extends DomainEvent {
    private final String carId;
    private final Instant occurredAt = Instant.now();

    /**
     * Create a CarMarkedAsUnavailableEvent for the specified car identifier.
     *
     * @param carId the identifier of the car that was marked as unavailable
     */
    public CarMarkedAsUnavailableEvent(String carId) {
        this.carId = carId;
    }

    /**
     * Provides the identifier of the car associated with this event.
     *
     * @return the car identifier
     */
    public String carId() {
        return carId;
    }

    /**
     * Provides the timestamp when the event occurred.
     *
     * @return the Instant representing when this event was created
     */
    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    /**
     * Gets the car_rental_app.domain event name for this event.
     *
     * @return the event name "CarMarkedAsUnavailableEvent"
     */
    @Override
    public String eventName() {
        return "CarMarkedAsUnavailableEvent";
    }
}
