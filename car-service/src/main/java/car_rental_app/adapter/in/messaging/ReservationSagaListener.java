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

    @Autowired
    public ReservationSagaListener(ReservationSagaHandler sagaHandler) {
        this.sagaHandler = sagaHandler;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.findAndRegisterModules();
    }

    private String trace() {
        return "trace=" + MDC.get("traceId") + " span=" + MDC.get("spanId");
    }

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
