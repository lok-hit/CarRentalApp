package car.rental.app.domain.event;

import java.time.Instant;
import java.util.Objects;

public record PaymentFailedEvent(
        String reservationId,
        String paymentId,
        String reason,
        String eventType,
        Instant occurredAt
) implements DomainEvent {

    public PaymentFailedEvent {
        reservationId = requireNotBlank(reservationId, "reservationId");
        paymentId = requireNotBlank(paymentId, "paymentId");
        reason = requireNotBlank(reason, "reason");
        eventType = requireNotBlank(eventType, "eventType");
    }

    public PaymentFailedEvent(String reservationId, String paymentId, String reason) {
        this(
                reservationId,
                paymentId,
                reason,
                "PaymentFailedEvent",
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
