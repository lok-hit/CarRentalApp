package car.rental.app.payment.domain.event;

public record RefundFailed(
        String paymentId,
        String reservationId,
        String reason,
        EventMetadata metadata
) {}
