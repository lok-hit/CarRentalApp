package car.rental.app.domain.event;

import java.time.Instant;

public record CarCreatedEvent(
        String carId,
        String brand,
        String model,
        Instant createdAt
) {}
