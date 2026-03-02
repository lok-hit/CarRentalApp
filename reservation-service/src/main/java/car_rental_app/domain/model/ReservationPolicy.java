package car_rental_app.domain.model;

import java.time.LocalDate;

public class ReservationPolicy {

    public void validate(DateRange dateRange) {

        if (dateRange == null) {
            throw new IllegalArgumentException("dateRange must not be null");
        }

        if (dateRange.from().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Reservation cannot start in the past");
        }
    }
}

