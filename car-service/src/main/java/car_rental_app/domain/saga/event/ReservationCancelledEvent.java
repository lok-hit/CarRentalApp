package car_rental_app.domain.saga.event;

public record ReservationCancelledEvent(String reservationId, String carId) {}
