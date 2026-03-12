package car.rental.app.adapter.out.messaging;


import car.rental.app.domain.port.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.KafkaException;
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
                        throw new KafkaException("Failed to publish saga event to topic 'saga-events'. " +
                    "Trace: " + prefix() + " Error: " + e.getMessage(), e);
        }
    }
}
