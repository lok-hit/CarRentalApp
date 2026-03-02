package car_rental_app.adapter.out.event;

import car_rental_app.application.port.DomainEventPublisher;
import car_rental_app.domain.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisherImpl implements DomainEventPublisher {
    
    private final car_rental_app.domain.port.EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public DomainEventPublisherImpl(car_rental_app.domain.port.EventPublisher eventPublisher, ObjectMapper objectMapper) {
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            eventPublisher.publish(eventJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish domain event", e);
        }
    }
}
