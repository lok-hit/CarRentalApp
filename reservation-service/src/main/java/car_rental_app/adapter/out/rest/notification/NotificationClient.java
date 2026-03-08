package car_rental_app.adapter.out.rest.notification;

import car_rental_app.adapter.out.persistence.notification.NotificationOutboxEntry;
import car_rental_app.adapter.out.persistence.notification.NotificationOutboxRepository;
import car_rental_app.adapter.out.rest.notification.dto.ReservationNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Component
public class NotificationClient {

    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);

    private final RestTemplate rest;
    private final NotificationOutboxRepository outbox;

    @Value("${notification-service.base-url}")
    private String baseUrl;

    public NotificationClient(RestTemplate rest, NotificationOutboxRepository outbox) {
        this.rest = rest;
        this.outbox = outbox;
    }

    public void sendReservationConfirmed(ReservationNotificationRequest request) {
        sendNotification(
                request,
                "/notifications/reservation-confirmed",
                "RESERVATION_CONFIRMED"
        );
    }

    public void sendReservationCancelled(ReservationNotificationRequest request) {
        sendNotification(
                request,
                "/notifications/reservation-cancelled",
                "RESERVATION_CANCELLED"
        );
    }

    private void sendNotification(
            ReservationNotificationRequest request,
            String endpoint,
            String type
    ) {
        String safeType =type !=null ? type.toLowerCase(): "unknown";
        try {
            rest.postForLocation(baseUrl + endpoint, request);
            log.info("Fire-and-forget: sent {} for {}",
                    safeType, request.reservationId());
        } catch (Exception e) {
            log.error("Fire-and-forget FAILED for {} {}", safeType, request.reservationId(), e);

            NotificationOutboxEntry entry = new NotificationOutboxEntry(
                    null,
                    request.reservationId(),
                    request.customerId(),
                    request.email(),
                    request.reason(),
                    type,
                    request.timestamp(),
                    0,
                    e.getMessage(),
                    Instant.now().plusSeconds(10)
            );

            outbox.save(entry);
        }
    }
}
