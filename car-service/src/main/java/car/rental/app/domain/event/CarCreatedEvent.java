package car.rental.app.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;

public class CarCreatedEvent extends DomainEvent {
    private final String carId;
    private final String category;
    private final BigDecimal price;
    private final Instant occurredAt = Instant.now();

    /**
     * Create a new CarCreatedEvent with the specified car identifier, category, and price.
     *
     * @param carId   the unique identifier of the car
     * @param category the car's category
     * @param price   the car's price
     */
    public CarCreatedEvent(String carId, String category, BigDecimal price) {
        this.carId = carId;
        this.category = category;
        this.price = price;
    }

    /**
     * Gets the car identifier.
     *
     * @return the car identifier
     */
    @JsonProperty("carId")
    public String carId() {
        return carId;
    }

    /**
     * Gets the car category.
     *
     * @return the category of the car
     */
    @JsonProperty("category")
    public String category() {
        return category;
    }

    /**
     * Returns the car's price.
     *
     * @return the car's price
     */
    @JsonProperty("price")
    public BigDecimal price() {
        return price;
    }

    /**
     * Timestamp when the event occurred.
     *
     * @return the event occurrence timestamp
     */
    @JsonProperty("occurredAt")
    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    /**
     * The canonical name of this car_rental_app.domain event.
     *
     * @return the event name "CarCreatedEvent"
     */
    @Override
    public String eventName() {
        return "CarCreatedEvent";
    }
}
