package car.rental.app.domain.event;

import java.time.Instant;
import java.util.Objects;

public record ReservationCreatedEvent(
        String reservationId,
        String carId,
        String customerId,
        String eventType,
        Instant occurredAt
) implements DomainEvent {

    public ReservationCreatedEvent {
        reservationId = requireNotBlank(reservationId, "reservationId");
        carId = requireNotBlank(carId, "carId");
        customerId = requireNotBlank(customerId, "customerId");
        eventType = requireNotBlank(eventType, "eventType");
    }

    public ReservationCreatedEvent(String reservationId, String carId, String customerId) {
        this(
                reservationId,
                carId,
                customerId,
                "ReservationCreatedEvent",
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
