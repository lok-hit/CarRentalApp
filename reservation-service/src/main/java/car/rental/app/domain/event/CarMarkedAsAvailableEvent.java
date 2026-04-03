package car.rental.app.domain.event;

import java.time.Instant;

public record CarMarkedAsAvailableEvent(
        String carId,
        Instant timestamp
) {}
