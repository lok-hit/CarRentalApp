package car_rental_app.domain.event;

import java.util.Objects;

public record ReservationCancelledEvent(
        String reservationId,
        String reason,
        String eventType
) {
    public ReservationCancelledEvent {
        Objects.requireNonNull(reservationId);
        Objects.requireNonNull(reason);
        Objects.requireNonNull(eventType);
    }

    public ReservationCancelledEvent(String reservationId, String reason) {
        this(reservationId, reason, "ReservationCancelledEvent");
    }
}
