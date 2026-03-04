package car_rental_app.adapter.out.rest.notification.dto;

import java.time.Instant;

public record ReservationNotificationRequest(
        String reservationId,
        String customerId,
        String email,
        Instant timestamp,
        String reason
) {}
