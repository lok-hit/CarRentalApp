package car.rental.app.domain.event;

import java.time.Instant;

public record CarMarkedAsUnavailableEvent(
        String carId,
        String reason,
        Instant timestamp
) {}
