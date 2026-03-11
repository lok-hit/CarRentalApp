package car.rental.app.payment.domain.event;

import car.rental.app.payment.domain.model.Money;

import java.time.Instant;

public record RefundCompleted(
        String paymentId,
        String reservationId,
        String customerId,
        Money amount,
        String providerPaymentId,
        Instant paidAt,
        EventMetadata metadata
) { }

