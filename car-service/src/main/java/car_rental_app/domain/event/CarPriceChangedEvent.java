package car_rental_app.domain.event;

import java.math.BigDecimal;
import java.time.Instant;

public class CarPriceChangedEvent extends DomainEvent {
    private final String carId;
    private final BigDecimal newPrice;
    private final Instant occurredAt = Instant.now();

    public CarPriceChangedEvent(String carId, BigDecimal newPrice) {
        this.carId = carId;
        this.newPrice = newPrice;
    }

    public String carId() {
        return carId;
    }

    public BigDecimal newPrice() {
        return newPrice;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public String eventName() {
        return "CarPriceChangedEvent";
    }
}
