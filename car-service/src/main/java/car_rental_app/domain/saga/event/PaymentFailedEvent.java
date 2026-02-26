package car_rental_app.domain.saga.event;

public record PaymentFailedEvent(String reservationId, String carId) {}
