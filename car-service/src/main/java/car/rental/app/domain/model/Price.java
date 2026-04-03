package car.rental.app.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public record Price(BigDecimal value) {
    public Price {
        Objects.requireNonNull(value, "Price value cannot be null");
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}