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

    /**
     * Create a KafkaSagaEventPublisher backed by the given KafkaTemplate.
     */
    public KafkaSagaEventPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    /**
     * Publish a ReservationCreatedEvent to the "reservation-created" Kafka topic.
     *
     * @param event the reservation-created event; its carId().value() is used as the Kafka message key
     * @return `true` if the event was sent to Kafka; completes exceptionally with the encountered exception if sending fails
     */
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

    /**
     * Publish a reservation-cancelled saga event to Kafka.
     *
     * Publishes the given event to the "reservation-cancelled" topic using the event's car ID as the message key.
     *
     * @param event the reservation cancelled event to publish; the Kafka message key is derived from event.carId().value()
     * @return `true` if the event was sent successfully; otherwise the returned future completes exceptionally
     */
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

    /**
     * Publishes a payment-failed saga event to the Kafka topic "payment-failed".
     *
     * @param event the payment failed event containing the car identifier and payload to publish
     * @return `true` when the event has been handed off to Kafka for sending; otherwise the returned future completes exceptionally with the underlying exception
     */
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

    /**
     * Builds a log message prefix containing trace and span identifiers from MDC.
     *
     * @return A formatted prefix string like "[traceId={traceId} spanId={spanId}] " (includes a trailing space).
     */
    private String prefix() {
        return "[traceId=" + MDC.get("traceId") + " spanId=" + MDC.get("spanId") + "] ";
    }
}
