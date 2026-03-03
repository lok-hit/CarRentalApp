package car_rental_app.domain.event;

import java.time.Instant;

public class CarMarkedAsAvailableEvent extends DomainEvent {
    private final String carId;
    private final Instant occurredAt = Instant.now();

    /**
     * Create an event representing that the car with the given identifier was marked as available.
     *
     * @param carId the identifier of the car
     */
    public CarMarkedAsAvailableEvent(String carId) {
        this.carId = carId;
    }

    /**
     * Gets the identifier of the car associated with this event.
     *
     * @return the car identifier
     */
    public String carId() {
        return carId;
    }

    /**
     * Gets the timestamp when the event occurred.
     *
     * @return the Instant representing when the event occurred
     */
    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    /**
     * Identifies this domain event with the name "CarMarkedAsAvailableEvent".
     *
     * @return the event name "CarMarkedAsAvailableEvent".
     */
    @Override
    public String eventName() {
        return "CarMarkedAsAvailableEvent";
    }
}
