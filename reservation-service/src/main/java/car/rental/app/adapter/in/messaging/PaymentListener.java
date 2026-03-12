package car.rental.app.adapter.in.messaging;

import car.rental.app.application.service.saga.ReservationSagaHandler;
import car.rental.app.domain.event.PaymentCompletedEvent;
import car.rental.app.domain.event.PaymentFailedEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentListener.class);

    private final ObjectMapper objectMapper;
    private final ReservationSagaHandler sagaHandler;

    public PaymentListener(ObjectMapper objectMapper, ReservationSagaHandler sagaHandler) {
        this.objectMapper = objectMapper;
        this.sagaHandler = sagaHandler;
    }

    @KafkaListener(
            topics = "${kafka.topics.payment-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onPaymentEvent(@Payload String message, ConsumerRecord<String, String> recorded) {
        log.info("Received payment event: {}", message);

        try {
            JsonNode json = objectMapper.readTree(message);
            String eventType = json.get("eventType").asText();

            switch (eventType) {
                case "PaymentCompletedEvent" -> {
                    PaymentCompletedEvent event = objectMapper.treeToValue(json, PaymentCompletedEvent.class);
                    sagaHandler.handlePaymentCompleted(event);
                }
                case "PaymentFailedEvent" -> {
                    PaymentFailedEvent event = objectMapper.treeToValue(json, PaymentFailedEvent.class);
                    sagaHandler.handlePaymentFailed(event);
                }
                default -> log.warn("Unknown eventType: {}", eventType);
            }

        } catch (Exception e) {
            log.error("Failed to process payment event", e);
        }
    }
}
