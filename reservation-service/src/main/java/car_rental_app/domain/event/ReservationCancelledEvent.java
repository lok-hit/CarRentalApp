package car_rental_app.domain.event;

import java.time.Instant;
import java.util.Objects;

public record ReservationCancelledEvent(
        String reservationId,
        String reason,
        String eventType,
        Instant occurredAt
) implements DomainEvent {

    public ReservationCancelledEvent {
        reservationId = requireNotBlank(reservationId, "reservationId");
        reason = requireNotBlank(reason, "reason");
        eventType = requireNotBlank(eventType, "eventType");
        occurredAt = Objects.requireNonNull(occurredAt, "occurredAt cannot be null");
    }

    public ReservationCancelledEvent(String reservationId, String reason) {
        this(
                reservationId,
                reason,
                "ReservationCancelledEvent",
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
