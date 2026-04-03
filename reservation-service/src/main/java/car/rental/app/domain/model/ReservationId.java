package car.rental.app.domain.model;

import java.util.Objects;
import java.util.UUID;

public record ReservationId(String value) {

    public ReservationId {
        Objects.requireNonNull(value, "ReservationId cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("ReservationId cannot be blank");
        }
        value = trimmed;
    }

    public static ReservationId generate() {
        return new ReservationId(UUID.randomUUID().toString());
    }
}
