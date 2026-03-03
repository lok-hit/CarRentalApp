package car_rental_app.domain.event;

import java.time.Instant;

public record CarMarkedAsUnavailableEvent(
        String carId,
        String reason,
        Instant timestamp
) {}
