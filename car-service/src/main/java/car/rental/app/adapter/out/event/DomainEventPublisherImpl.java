package car.rental.app.adapter.out.event;

import car.rental.app.application.port.DomainEventPublisher;
import car.rental.app.domain.event.DomainEvent;
import car.rental.app.domain.port.EventPublisher;
import car.rental.app.exceptions.DomainEventException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisherImpl implements DomainEventPublisher {
    
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public DomainEventPublisherImpl(EventPublisher eventPublisher, ObjectMapper objectMapper) {
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            eventPublisher.publish(eventJson);
        } catch (Exception e) {
            throw new DomainEventException("Failed to publish car_rental_app.domain event", e);
        }
    }
}
