package car_rental_app.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public record DateRange(LocalDate from, LocalDate to) {

    public DateRange {
        Objects.requireNonNull(from, "Start date cannot be null");
        Objects.requireNonNull(to, "End date cannot be null");

        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    public boolean overlaps(DateRange other) {
        return !(other.to().isBefore(from) || other.from().isAfter(to));
    }
}

