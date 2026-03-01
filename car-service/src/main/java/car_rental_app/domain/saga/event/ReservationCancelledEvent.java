package car_rental_app.domain.saga.event;

import car_rental_app.domain.model.CarId;

public record ReservationCancelledEvent(String reservationId, CarId carId) implements DomainEvent {}
