package car_rental_app.domain.event;

import java.util.Objects;

public record PaymentFailedEvent(
        String reservationId,
        String paymentId,
        String reason,
        String eventType
) {
    public PaymentFailedEvent {
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(paymentId, "paymentId cannot be null");
        Objects.requireNonNull(reason, "reason cannot be null");
        Objects.requireNonNull(eventType, "eventType cannot be null");

        if (reservationId.isBlank()) throw new IllegalArgumentException("reservationId cannot be blank");
        if (paymentId.isBlank()) throw new IllegalArgumentException("paymentId cannot be blank");
        if (reason.isBlank()) throw new IllegalArgumentException("reason cannot be blank");
        if (eventType.isBlank()) throw new IllegalArgumentException("eventType cannot be blank");
    }

    public PaymentFailedEvent(String reservationId, String paymentId, String reason) {
        this(reservationId, paymentId, reason, "PaymentFailedEvent");
    }
}
