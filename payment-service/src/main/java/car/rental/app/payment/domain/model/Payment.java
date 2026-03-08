package car.rental.app.payment.domain.model;


import java.time.Instant;

public record Payment(
        String id,
        String reservationId,
        String customerId,
        Money amount,
        PaymentStatus status,
        Instant paidAt,
        Instant createdAt,
        Instant updatedAt
) {}

