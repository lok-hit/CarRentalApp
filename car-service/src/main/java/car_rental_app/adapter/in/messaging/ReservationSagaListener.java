package car_rental_app.adapter.in.messaging;

import car_rental_app.application.service.saga.ReservationSagaHandler;
import car_rental_app.domain.saga.event.PaymentCompletedEvent;
import car_rental_app.domain.saga.event.PaymentConfirmedEvent;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ReservationSagaListener {
    private static final Logger log = Logger.getLogger(ReservationSagaListener.class.getName());
    private final ReservationSagaHandler sagaHandler;
    private final ObjectMapper objectMapper;

    /**
     * Create a ReservationSagaListener and configure its JSON deserialization support.
     *
     * Initializes the listener with the provided saga handler and prepares an ObjectMapper
     * (registering the JavaTimeModule and auto-registering available modules) for parsing
     * incoming saga event JSON.
     *
     * @param sagaHandler the handler that processes saga events received by this listener
     */
    @Autowired
    public ReservationSagaListener(ReservationSagaHandler sagaHandler) {
        this.sagaHandler = sagaHandler;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.findAndRegisterModules();
    }

    /**
     * Builds a compact trace identifier string from the MDC context.
     *
     * <p>The returned string has the form "trace=&lt;traceId&gt; span=&lt;spanId&gt;". If either
     * MDC entry is absent, its place in the string will be `null`.</p>
     *
     * @return the formatted trace string containing `traceId` and `spanId`
     */
    private String trace() {
        return "trace=" + MDC.get("traceId") + " span=" + MDC.get("spanId");
    }

    /**
     * Handles a reservation-created Kafka message by parsing the JSON and delegating the resulting event to the saga handler.
     *
     * Parses the given JSON into a ReservationCreatedEvent, logs receipt with trace information, and invokes sagaHandler.onReservationCreated(...).
     * If processing fails the method logs a severe error and does not rethrow the exception.
     *
     * @param json the raw JSON payload of a ReservationCreatedEvent
     */
    @KafkaListener(topics = "reservation-created")
    public void onReservationCreated(String json) {
        try {
            ReservationCreatedEvent event = objectMapper.readValue(json, ReservationCreatedEvent.class);
            log.info(() -> "[" + trace() + "] Kafka: ReservationCreated car=" + event.carId());
            sagaHandler.onReservationCreated(event);
        } catch (Exception e) {
            log.severe("Failed to process ReservationCreatedEvent: " + e.getMessage());
        }
    }

    /**
     * Processes a "reservation-cancelled" Kafka message by deserializing the JSON payload into a ReservationCancelledEvent and handling the reservation cancellation.
     *
     * @param json JSON payload representing a ReservationCancelledEvent (expected structure matches {@code ReservationCancelledEvent})
     */
    @KafkaListener(topics = "reservation-cancelled")
    public void onReservationCancelled(String json) {
        try {
            ReservationCancelledEvent event = objectMapper.readValue(json, ReservationCancelledEvent.class);
            log.info(() -> "[" + trace() + "] Kafka: ReservationCancelled car=" + event.carId());
            sagaHandler.onReservationCancelled(event);
        } catch (Exception e) {
            log.severe("Failed to process ReservationCancelledEvent: " + e.getMessage());
        }
    }

    /**
     * Handle a Kafka "payment-completed" message by parsing the provided JSON into a PaymentCompletedEvent,
     * converting it into a PaymentConfirmedEvent, and forwarding it to the reservation saga handler.
     *
     * If parsing or processing fails, the error is logged.
     *
     * @param json the raw JSON payload from the "payment-completed" topic representing a PaymentCompletedEvent
     */
    @KafkaListener(topics = "payment-completed")
    public void onPaymentCompleted(String json) {
        log.info("Received PaymentCompleted JSON: " + json);
        try {
            PaymentCompletedEvent event = objectMapper.readValue(json, PaymentCompletedEvent.class);
            log.info("Received PaymentCompletedEvent: " + event);
            log.info(() -> "[" + trace() + "] Kafka: PaymentCompleted car=" + event.carId());
            // Convert PaymentCompletedEvent to PaymentConfirmedEvent
            PaymentConfirmedEvent confirmedEvent = new PaymentConfirmedEvent(
                event.reservationId(),
                event.carId(),
                event.userId(),
                event.paymentStatus()
            );
            log.info("Created PaymentConfirmedEvent: " + confirmedEvent);
            sagaHandler.onPaymentConfirmed(confirmedEvent);
            log.info("Successfully processed PaymentConfirmedEvent");
        } catch (Exception e) {
            log.severe("Failed to process PaymentCompletedEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses a PaymentFailedEvent from the provided JSON, logs the event, and triggers reservation failure handling.
     *
     * If parsing or processing fails, a severe error message is logged.
     *
     * @param json JSON payload representing a PaymentFailedEvent
     */
    @KafkaListener(topics = "payment-failed")
    public void onPaymentFailed(String json) {
        try {
            PaymentFailedEvent event = objectMapper.readValue(json, PaymentFailedEvent.class);
            log.info(() -> "[" + trace() + "] Kafka: PaymentFailed car=" + event.carId());
            sagaHandler.onReservationFailed(event);
        } catch (Exception e) {
            log.severe("Failed to process PaymentFailedEvent: " + e.getMessage());
        }
    }
}
