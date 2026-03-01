package car_rental_app.domain.port;

import car_rental_app.domain.saga.event.DomainEvent;

public interface EventPublisher { void publish(String topic, DomainEvent event); }
