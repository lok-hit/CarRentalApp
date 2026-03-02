package car_rental_app.domain.model;

import java.util.Objects;

public record CarId(String value) {

    public CarId {
        Objects.requireNonNull(value, "CarId cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("CarId cannot be blank");
        }
    }

    public static CarId of(String id) {
        return new CarId(id);
    }
}
