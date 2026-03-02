package car_rental_app.domain.event;

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

    public String carId() {
        return carId;
    }

    public String category() {
        return category;
    }

    public BigDecimal price() {
        return price;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public String eventName() {
        return "CarCreatedEvent";
    }
}
