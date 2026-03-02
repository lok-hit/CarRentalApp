package car_rental_app.application.port;

import car_rental_app.domain.event.DomainEvent;

public interface DomainEventPublisher {
    /**
 * Publish the given domain event to interested recipients.
 *
 * @param event the domain event to publish
 */
void publish(DomainEvent event);
}

