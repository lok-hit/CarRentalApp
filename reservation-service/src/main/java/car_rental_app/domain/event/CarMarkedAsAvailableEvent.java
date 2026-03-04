package car_rental_app.domain.event;

import java.time.Instant;

public record CarMarkedAsAvailableEvent(
        String carId,
        Instant timestamp
) {}
