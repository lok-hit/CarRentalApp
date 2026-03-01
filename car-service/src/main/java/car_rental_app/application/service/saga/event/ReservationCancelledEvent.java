package car_rental_app.application.service.saga.event;

import car_rental_app.domain.saga.event.DomainEvent;

import java.time.Instant;

public record ReservationCancelledEvent(String reservationId, String carId, Instant timestamp ) implements DomainEvent {}
