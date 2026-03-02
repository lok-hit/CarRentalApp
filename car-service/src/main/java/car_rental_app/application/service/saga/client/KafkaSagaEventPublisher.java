package car_rental_app.application.service.saga.client;


import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Component
public class KafkaSagaEventPublisher implements SagaEventPublisher {

    private final static Logger log = Logger.getLogger(KafkaSagaEventPublisher.class.getName());
    private final KafkaTemplate<String, Object> kafka;

    public KafkaSagaEventPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    @Override
    public CompletableFuture<Boolean> publishReservationCreated(ReservationCreatedEvent event) {

        log.info(() -> prefix() + "Received ReservationCreatedEvent: " + event);
        try {
            kafka.send("reservation-created", event.carId().value(), event);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<Boolean> publishReservationCancelled(ReservationCancelledEvent event) {

        log.info(()-> prefix() + "Received ReservationCancelledEvent" + event);
        try {
            kafka.send("reservation-cancelled", event.carId().value(), event);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<Boolean> publishPaymentFailed(PaymentFailedEvent event) {

        log.info(()-> prefix() + "Received payment failed event" + event);
        try {
            kafka.send("payment-failed", event.carId().value(), event);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private String prefix() {
        return "[traceId=" + MDC.get("traceId") + " spanId=" + MDC.get("spanId") + "] ";
    }
}
