package car.rental.app.domain.model;

import java.util.Objects;
import java.util.UUID;

public record CarId(String value) {

    public CarId {
        Objects.requireNonNull(value, "CarId cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("CarId cannot be blank");
        }
        value = trimmed;
    }

    public static CarId generate() {
        return new CarId(UUID.randomUUID().toString());
    }
}
