package car_rental_app.adapter.out.persistence.notification;

import java.time.Instant;

public record NotificationOutboxEntry(
        String reservationId,
        String customerId,
        String email,
        String reason,
        String type,
        Instant timestamp,
        String errorMessage
) {}
