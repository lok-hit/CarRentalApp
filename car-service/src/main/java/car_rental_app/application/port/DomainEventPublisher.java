package car_rental_app.application.port;

import car_rental_app.domain.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

