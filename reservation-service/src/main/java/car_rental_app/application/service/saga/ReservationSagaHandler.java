package car_rental_app.application.service.saga;

import car_rental_app.application.service.ReservationApplicationService;
import car_rental_app.domain.event.PaymentCompletedEvent;
import car_rental_app.domain.event.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReservationSagaHandler {

    private static final Logger log = LoggerFactory.getLogger(ReservationSagaHandler.class);

    private final ReservationApplicationService reservationService;

    public ReservationSagaHandler(ReservationApplicationService reservationService) {
        this.reservationService = reservationService;
    }

    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Saga: Payment completed for reservationId={}", event.reservationId());
        reservationService.confirmReservation(event.reservationId());
    }

    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Saga: Payment failed for reservationId={} reason={}", event.reservationId(), event.reason());
        reservationService.cancelReservation(event.reservationId());
    }
}
