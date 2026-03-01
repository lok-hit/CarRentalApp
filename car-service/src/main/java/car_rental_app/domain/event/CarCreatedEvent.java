package car_rental_app.domain.event;

import car_rental_app.domain.saga.event.DomainEvent;

import java.math.BigDecimal;

public record CarCreatedEvent(String carId, String category, BigDecimal price ) implements DomainEvent {}
