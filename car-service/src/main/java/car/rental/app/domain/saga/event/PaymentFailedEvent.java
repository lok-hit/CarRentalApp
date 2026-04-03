package car.rental.app.domain.saga.event;

import car.rental.app.domain.model.CarId;

public record PaymentFailedEvent(String reservationId, CarId carId, String user) {}
