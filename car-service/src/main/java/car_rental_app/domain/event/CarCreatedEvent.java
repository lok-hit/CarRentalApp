package car_rental_app.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;

public class CarCreatedEvent extends DomainEvent {
    private final String carId;
    private final String category;
    private final BigDecimal price;
    private final Instant occurredAt = Instant.now();

    public CarCreatedEvent(String carId, String category, BigDecimal price) {
        this.carId = carId;
        this.category = category;
        this.price = price;
    }

    @JsonProperty("carId")
    public String carId() {
        return carId;
    }

    @JsonProperty("category")
    public String category() {
        return category;
    }

    @JsonProperty("price")
    public BigDecimal price() {
        return price;
    }

    @JsonProperty("occurredAt")
    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public String eventName() {
        return "CarCreatedEvent";
    }
}
