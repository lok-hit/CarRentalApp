package car_rental_app.domain.model;

import car_rental_app.domain.event.CarCreatedEvent;
import car_rental_app.domain.event.CarMarkedAsAvailableEvent;
import car_rental_app.domain.event.CarMarkedAsUnavailableEvent;
import car_rental_app.domain.event.CarPriceChangedEvent;
import car_rental_app.domain.saga.event.DomainEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Car {
    private final CarId id;
    private CarCategory category;
    private Price price;
    private AvailabilityStatus status;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Car(CarId id, CarCategory category, Price price) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.status = AvailabilityStatus.AVAILABLE;
        domainEvents.add(new CarCreatedEvent(id.value(), category.name(), price.value()));
    }

    public void markAsUnavailable() {
        if (status == AvailabilityStatus.UNAVAILABLE) return;
        this.status = AvailabilityStatus.UNAVAILABLE;
        domainEvents.add(new CarMarkedAsUnavailableEvent(id.value()));
    }

    public void markAsAvailable() {
        if (status == AvailabilityStatus.AVAILABLE) return;
        this.status = AvailabilityStatus.AVAILABLE;
        domainEvents.add(new CarMarkedAsAvailableEvent(id.value()));
    }

    public void changePrice(Price newPrice) {
        this.price = newPrice;
        domainEvents.add(new CarPriceChangedEvent(id.value(), newPrice.value()));
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public CarId id() {
        return id;
    }

    public CarCategory category() {
        return category;
    }

    public Price price() {
        return price;
    }

    public AvailabilityStatus status() {
        return status;
    }
}
