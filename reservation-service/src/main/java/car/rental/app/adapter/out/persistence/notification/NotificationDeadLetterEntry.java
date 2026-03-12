package car.rental.app.adapter.out.persistence.notification;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("notification_dead_letter")
public record NotificationDeadLetterEntry(
        String id,
        String reservationId,
        String customerId,
        String email,
        String reason,
        String type,
        Instant timestamp,
        int retryCount,
        String lastError,
        Instant failedAt
) {}

