package car.rental.app.payment.domain.command;

public record RefundRequested(
        String paymentId,
        String correlationId,
        String idempotencyKey
) {}
