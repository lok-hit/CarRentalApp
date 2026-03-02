package car_rental_app.domain.model;

import car_rental_app.domain.event.*;

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

    /**
     * Updates the car's price and appends a CarPriceChangedEvent to the domain event list.
     *
     * @param newPrice the new price for the car
     */
    public void changePrice(Price newPrice) {
        this.price = newPrice;
        domainEvents.add(new CarPriceChangedEvent(id.value(), newPrice.value()));
    }

    /**
     * Provides and clears the car's queued domain events.
     *
     * Returns an immutable copy of the current domain events and clears the internal event list so subsequent calls will not return the same events.
     *
     * @return an immutable List of DomainEvent objects that were queued for this car
     */
    public List<DomainEvent> drainDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }


    /**
     * Retrieve the car's identifier.
     *
     * @return the CarId of this car
     */
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
