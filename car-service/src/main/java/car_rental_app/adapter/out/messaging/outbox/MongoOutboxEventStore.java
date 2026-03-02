package car_rental_app.adapter.out.messaging.outbox;

import car_rental_app.domain.event.DomainEvent;
import car_rental_app.domain.port.OutboxEventStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.logging.Logger;

@Component
public class MongoOutboxEventStore implements OutboxEventStore {

    private static final Logger log = Logger.getLogger(MongoOutboxEventStore.class.getName());
    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public MongoOutboxEventStore(OutboxEventRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void saveEvent(String reservationId, DomainEvent event) {
        OutboxEventDocument document = new OutboxEventDocument(reservationId,
                event.getClass().getSimpleName(), serialize(event), "PENDING", Instant.now());
        log.info(() -> prefix() + "Saving event to Mongo: " + event);
        repository.save(document);
    }

    private String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.severe(() -> prefix() + "Failed to serialize event: " + e.getMessage());
            throw new RuntimeException("Failed to serialize domain event: " + event, e);
        }
    }

    private String prefix() {
        return "[traceId=" + MDC.get("traceId") + " spanId=" + MDC.get("spanId") + "] ";
    }
}
