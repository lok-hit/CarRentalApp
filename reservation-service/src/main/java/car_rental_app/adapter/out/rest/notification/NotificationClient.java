package car_rental_app.adapter.out.rest.notification;

import car_rental_app.adapter.out.persistence.notification.NotificationOutboxEntry;
import car_rental_app.adapter.out.persistence.notification.NotificationOutboxRepository;
import car_rental_app.adapter.out.rest.notification.dto.ReservationNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        try {
            rest.postForLocation(
                    baseUrl + "/notifications/reservation-confirmed",
                    request
            );
            log.info("Fire-and-forget: sent reservation-confirmed for {}", request.reservationId());
        } catch (Exception e) {
            log.error("Fire-and-forget FAILED for reservation-cancelled {}", request.reservationId(), e);
            outbox.save(new NotificationOutboxEntry(request.reservationId(), request.customerId(),
                    request.email(), request.reason(), "RESERVATION_CANCELLED",
                    request.timestamp(), e.getMessage()));
        }
    }

    public void sendReservationCancelled(ReservationNotificationRequest request) {
        try {
            rest.postForLocation(
                    baseUrl + "/notifications/reservation-cancelled",
                    request
            );
            log.info("Fire-and-forget: sent reservation-cancelled for {}", request.reservationId());
        } catch (Exception e) {
            log.error("Fire-and-forget FAILED for reservation-confirmed {}", request.reservationId(), e);
            outbox.save(new NotificationOutboxEntry(request.reservationId(), request.customerId(),
                    request.email(), request.reason(), "RESERVATION_CONFIRMED",
                    request.timestamp(), e.getMessage()));
        }
    }
}
