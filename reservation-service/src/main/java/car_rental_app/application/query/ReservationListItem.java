package car_rental_app.application.query;

public record ReservationListItem(
        String reservationId,
        String carId,
        String status
) {}
