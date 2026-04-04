package car.rental.app.notification.dto;

import java.time.Instant;

public record NotificationRequest(
        String reservationId,
        String customerId,
        String email,
        Instant timestamp,
        String reason
) {}
