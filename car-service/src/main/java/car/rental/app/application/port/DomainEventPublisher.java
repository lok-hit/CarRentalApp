package car.rental.app.application.port;

import car.rental.app.domain.event.DomainEvent;

public interface DomainEventPublisher {
    /**
 * Publish the given car_rental_app.domain event to interested recipients.
 *
 * @param event the car_rental_app.domain event to publish
 */
void publish(DomainEvent event);
}

