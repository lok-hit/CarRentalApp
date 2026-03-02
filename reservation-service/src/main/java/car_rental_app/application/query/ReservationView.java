package car_rental_app.application.query;

import java.time.LocalDate;

public record ReservationView(
        String reservationId,
        String carId,
        String customerId,
        LocalDate from,
        LocalDate to,
        String status,
        String price
) {}
