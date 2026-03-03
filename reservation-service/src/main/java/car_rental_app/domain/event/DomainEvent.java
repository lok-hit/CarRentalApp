package car_rental_app.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
