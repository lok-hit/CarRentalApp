package car_rental_app.application.service.saga.event;

import java.time.Instant;

public record ReservationCancelledEvent(String reservationId, String carId, Instant timestamp ) {}
