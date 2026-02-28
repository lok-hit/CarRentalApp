package car_rental_app.application.service.saga.event;

import java.time.Instant;

public record ReservationCreatedEvent(String reservationId, String carId, String userId, Instant timestamp) {
}
