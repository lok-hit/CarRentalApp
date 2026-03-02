package car_rental_app.adapter.in.messaging;

import car_rental_app.application.service.saga.ReservationSagaHandler;
import car_rental_app.domain.saga.event.PaymentCompletedEvent;
import car_rental_app.domain.saga.event.PaymentConfirmedEvent;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ReservationSagaListener {
    private static final Logger log = Logger.getLogger(ReservationSagaListener.class.getName());
    private final ReservationSagaHandler sagaHandler;

    @Autowired
    public ReservationSagaListener(ReservationSagaHandler sagaHandler) {
        this.sagaHandler = sagaHandler;
    }

    private String trace() {
        return "trace=" + MDC.get("traceId") + " span=" + MDC.get("spanId");
    }

    @KafkaListener(topics = "reservation-created")
    public void onReservationCreated(ReservationCreatedEvent event) {
        log.info(() -> "[" + trace() + "] Kafka: ReservationCreated car=" + event.carId());
        sagaHandler.onReservationCreated(event);
    }

    @KafkaListener(topics = "reservation-cancelled")
    public void onReservationCancelled(ReservationCancelledEvent event) {
        log.info(() -> "[" + trace() + "] Kafka: ReservationCancelled car=" + event.carId());
        sagaHandler.onReservationCancelled(event);
    }

    @KafkaListener(topics = "payment-completed")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info(() -> "[" + trace() + "] Kafka: PaymentCompleted car=" + event.carId());
        // Convert PaymentCompletedEvent to PaymentConfirmedEvent
        PaymentConfirmedEvent confirmedEvent = new PaymentConfirmedEvent(
            event.reservationId(),
            event.carId(),
            event.userId(),
            event.paymentStatus()
        );
        sagaHandler.onPaymentConfirmed(confirmedEvent);
    }

    @KafkaListener(topics = "payment-failed")
    public void onPaymentFailed(PaymentFailedEvent event) {
        log.info(() -> "[" + trace() + "] Kafka: PaymentFailed car=" + event.carId());
        sagaHandler.onReservationFailed(event);
    }
}
