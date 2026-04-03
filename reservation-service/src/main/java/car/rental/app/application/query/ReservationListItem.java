package car.rental.app.application.query;

public record ReservationListItem(
        String reservationId,
        String carId,
        String status
) {}
