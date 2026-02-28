package car_rental_app.domain.model;

import java.math.BigDecimal;

public record Price(java.math.BigDecimal value) {
    public Price {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
              throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
