package car_rental_app.adapter.out.messaging;


import car_rental_app.domain.port.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component("domainEventPublisher")
public class KafkaSagaEventPublisher implements EventPublisher {

    private final Logger log = Logger.getLogger(KafkaSagaEventPublisher.class.getName());
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper mapper;

    public KafkaSagaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }


    private String prefix() {
        return "[traceId=" + MDC.get("traceId") + " spanId=" + MDC.get("spanId") + "] ";
    }

    @Override
    public void publish(String topic) {
        try {
            String json = mapper.writeValueAsString("saga-event");
            log.info(() -> prefix() + "[SAGA-PUBLISHER] Publishing saga event: type=" + ", payload=" + json);
            kafkaTemplate.send("saga-events", json);
            log.info(() -> prefix() + "[SAGA-PUBLISHER] Saga event published successfully.");
        } catch (Exception e) {
            log.severe(() -> prefix() + "[SAGA-PUBLISHER] Failed to publish saga event: " + e.getMessage());
            throw new RuntimeException("Failed to publish saga event", e);
        }
    }
}
