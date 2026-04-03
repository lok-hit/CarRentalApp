package car.rental.app.payment.application.dto;


import car.rental.app.payment.domain.model.Money;

public record PaymentRequestDto(
        String reservationId,
        String customerId,
        Money amount
) {}

