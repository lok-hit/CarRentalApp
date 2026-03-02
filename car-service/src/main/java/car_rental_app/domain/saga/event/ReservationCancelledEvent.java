package car_rental_app.domain.saga.event;

import car_rental_app.domain.model.CarId;

import java.time.Instant;
import java.util.Objects;

public record ReservationCancelledEvent(String reservationId, CarId carId, String userId, Instant timestamp) {
    public ReservationCancelledEvent {
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(carId, "carId cannot be null");
        Objects.requireNonNull(timestamp, "timestamp cannot be null");
        if (reservationId.isBlank()) {
            throw new IllegalArgumentException("reservationId cannot be blank");
        }
        if (carId.value().isBlank()) {
            throw new IllegalArgumentException("carId cannot be blank");
        }
    }
}