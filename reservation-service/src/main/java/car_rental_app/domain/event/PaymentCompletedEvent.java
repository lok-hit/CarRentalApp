package car_rental_app.domain.event;

import java.util.Objects;

public record PaymentCompletedEvent(
        String reservationId,
        String paymentId,
        String eventType
) {
    public PaymentCompletedEvent {
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(paymentId, "paymentId cannot be null");
        Objects.requireNonNull(eventType, "eventType cannot be null");

        if (reservationId.isBlank()) throw new IllegalArgumentException("reservationId cannot be blank");
        if (paymentId.isBlank()) throw new IllegalArgumentException("paymentId cannot be blank");
        if (eventType.isBlank()) throw new IllegalArgumentException("eventType cannot be blank");
    }

    public PaymentCompletedEvent(String reservationId, String paymentId) {
        this(reservationId, paymentId, "PaymentCompletedEvent");
    }
}
