package car_rental_app.domain.event;

import java.util.Objects;

public record ReservationConfirmedEvent(
        String reservationId,
        String eventType
) {
    public ReservationConfirmedEvent {
        Objects.requireNonNull(reservationId);
        Objects.requireNonNull(eventType);
    }

    public ReservationConfirmedEvent(String reservationId) {
        this(reservationId, "ReservationConfirmedEvent");
    }
}
