package car_rental_app.domain.event;

import java.math.BigDecimal;

public record CarCreatedEvent(String carId, String category, BigDecimal price ) {}
