package car.rental.app.payment.domain.command;


import car.rental.app.payment.domain.model.Money;

public record PaymentRequested(
        String reservationId,
        String customerId,
        Money amount,
        String correlationId,
        String idempotencyKey
) {}
