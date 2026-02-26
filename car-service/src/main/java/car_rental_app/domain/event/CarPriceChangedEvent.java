package car_rental_app.domain.event;

import java.math.BigDecimal;

public record CarPriceChangedEvent(String carId, BigDecimal newPrice ) {}
