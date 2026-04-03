package car.rental.app.domain.event;

import java.math.BigDecimal;
import java.time.Instant;

public class CarPriceChangedEvent extends DomainEvent {
    private final String carId;
    private final BigDecimal newPrice;
    private final Instant occurredAt = Instant.now();

    /**
     * Creates a CarPriceChangedEvent for the given car with the specified new price.
     *
     * The event's {@code occurredAt} timestamp is set to the current instant when the instance is created.
     *
     * @param carId    identifier of the car whose price changed
     * @param newPrice the updated price for the car
     */
    public CarPriceChangedEvent(String carId, BigDecimal newPrice) {
        this.carId = carId;
        this.newPrice = newPrice;
    }

    /**
     * Retrieve the identifier of the car associated with this event.
     *
     * @return the car identifier
     */
    public String carId() {
        return carId;
    }

    /**
     * Get the updated price for the car.
     *
     * @return the new price for the car as a {@code BigDecimal}
     */
    public BigDecimal newPrice() {
        return newPrice;
    }

    /**
     * Gets the timestamp when the car_rental_app.domain event occurred.
     *
     * @return the Instant representing when this event occurred
     */
    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    /**
     * Identifies this car_rental_app.domain event with its canonical name.
     *
     * @return the canonical event name "CarPriceChangedEvent"
     */
    @Override
    public String eventName() {
        return "CarPriceChangedEvent";
    }
}
