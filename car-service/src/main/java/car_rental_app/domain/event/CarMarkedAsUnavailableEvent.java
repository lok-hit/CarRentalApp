package car_rental_app.domain.event;

import car_rental_app.domain.saga.event.DomainEvent;

public record CarMarkedAsUnavailableEvent(String carId ) implements DomainEvent {}
