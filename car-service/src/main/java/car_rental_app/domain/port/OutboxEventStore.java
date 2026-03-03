package car_rental_app.domain.port;

import car_rental_app.domain.event.DomainEvent;

public interface OutboxEventStore {
    /**
 * Persist the given domain event in the outbox for the specified aggregate.
 *
 * @param aggregateId the identifier of the aggregate that produced the event
 * @param event       the DomainEvent instance to persist in the outbox
 */
void saveEvent(String aggregateId, DomainEvent event);
}