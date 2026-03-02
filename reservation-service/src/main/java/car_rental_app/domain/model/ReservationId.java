package car_rental_app.domain.model;

import java.util.Objects;

public record ReservationId(String value) {

    public ReservationId {
        Objects.requireNonNull(value, "ReservationId cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("ReservationId cannot be blank");
        }
    }

    public static ReservationId of(String id) {
        return new ReservationId(id);
    }
}
