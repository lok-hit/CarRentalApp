package car.rental.app.payment.domain.event;

import car.rental.app.payment.domain.model.Payment;

public record PaymentCompleted(
        Payment payment,
        EventMetadata metadata
) {}
