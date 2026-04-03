package car.rental.app.notification.service;

import car.rental.app.notification.dto.NotificationRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final Counter confirmedCounter;
    private final Counter cancelledCounter;

    public NotificationService(MeterRegistry registry) {
        this.confirmedCounter = registry.counter("notifications.sent", "type", "confirmed");
        this.cancelledCounter = registry.counter("notifications.sent", "type", "cancelled");
    }

    public void sendReservationConfirmed(NotificationRequest request) {
        log.info("[EMAIL] Reservation confirmed: reservationId={} customerId={} email={}",
                request.reservationId(), request.customerId(), request.email());
        confirmedCounter.increment();
    }

    public void sendReservationCancelled(NotificationRequest request) {
        log.info("[EMAIL] Reservation cancelled: reservationId={} customerId={} email={} reason={}",
                request.reservationId(), request.customerId(), request.email(), request.reason());
        cancelledCounter.increment();
    }
}
