package car_rental_app.domain.model;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(String value) {

    public CustomerId {
        Objects.requireNonNull(value, "CustomerId cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("CustomerId cannot be blank");
        }
        value = trimmed;
    }

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID().toString());
    }
}
