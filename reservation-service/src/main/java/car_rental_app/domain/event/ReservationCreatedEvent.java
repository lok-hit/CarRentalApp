package car_rental_app.domain.event;

import java.util.Objects;

public record ReservationCreatedEvent(
        String reservationId,
        String carId,
        String customerId,
        String price,
        String eventType
) {
    public ReservationCreatedEvent {
        Objects.requireNonNull(reservationId);
        Objects.requireNonNull(carId);
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(price);
        Objects.requireNonNull(eventType);
    }

    public ReservationCreatedEvent(String reservationId, String carId, String customerId, String price) {
        this(reservationId, carId, customerId, price, "ReservationCreatedEvent");
    }
}
