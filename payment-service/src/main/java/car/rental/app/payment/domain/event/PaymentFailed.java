package car.rental.app.payment.domain.event;

public record PaymentFailed(
        String paymentId,
        String reservationId,
        String reason
) {
}

