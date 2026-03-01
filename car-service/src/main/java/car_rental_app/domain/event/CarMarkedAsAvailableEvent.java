package car_rental_app.domain.event;

import car_rental_app.domain.saga.event.DomainEvent;

public record CarMarkedAsAvailableEvent(String carId ) implements DomainEvent {}
