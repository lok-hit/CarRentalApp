package car_rental_app.domain.saga.event;

import car_rental_app.domain.model.CarId;

public record ReservationCreatedEvent(String reservationId, CarId carId) {}