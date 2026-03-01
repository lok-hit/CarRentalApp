package car_rental_app.domain.port;

import car_rental_app.domain.saga.event.DomainEvent;

public interface OutboxEventStore {
    void saveEvent(String aggregateId, DomainEvent event);
}