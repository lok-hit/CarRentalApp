package car_rental_app.domain.event;

import java.time.Instant;

public abstract class DomainEvent {
    private final Instant occurredAt = Instant.now();

    public Instant occurredAt() {
        return occurredAt;
    }

    public abstract String eventName();

    public String topic() {
        return eventName();
    }
}