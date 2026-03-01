package car_rental_app.application.service.saga.event;

import car_rental_app.domain.saga.event.DomainEvent;

import java.time.Instant;

public record PaymentFailedEvent(String paymentId,
                                 String reservationId,
                                 String carId,
                                 String reason,
                                 Instant timestamp ) implements DomainEvent {}
