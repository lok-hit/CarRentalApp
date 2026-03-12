package car.rental.app.domain.event;

import java.time.Instant;
import java.util.Objects;

public record ReservationConfirmedEvent(
        String reservationId,
        String eventType,
        Instant occurredAt
) implements DomainEvent {

    public ReservationConfirmedEvent {
        reservationId = requireNotBlank(reservationId, "reservationId");
        eventType = requireNotBlank(eventType, "eventType");
    }

    public ReservationConfirmedEvent(String reservationId) {
        this(
                reservationId,
                "ReservationConfirmedEvent",
                Instant.now()
        );
    }

    private static String requireNotBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return trimmed;
    }
}
