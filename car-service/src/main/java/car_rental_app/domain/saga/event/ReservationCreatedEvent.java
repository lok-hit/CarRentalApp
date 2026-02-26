package car_rental_app.domain.saga.event;

public record ReservationCreatedEvent(String reservationId, String carId) {}