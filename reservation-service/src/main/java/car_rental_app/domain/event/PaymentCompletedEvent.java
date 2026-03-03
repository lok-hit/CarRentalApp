package car_rental_app.domain.event;

import java.time.Instant;
import java.util.Objects;

public record PaymentCompletedEvent(
        String reservationId,
        String paymentId,
        String eventType,
        Instant occurredAt
) implements DomainEvent {

    public PaymentCompletedEvent {
        reservationId = requireNotBlank(reservationId, "reservationId");
        paymentId = requireNotBlank(paymentId, "paymentId");
        eventType = requireNotBlank(eventType, "eventType");
        occurredAt = Objects.requireNonNull(occurredAt, "occurredAt cannot be null");
    }

    public PaymentCompletedEvent(String reservationId, String paymentId) {
        this(
                reservationId,
                paymentId,
                "PaymentCompletedEvent",
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
