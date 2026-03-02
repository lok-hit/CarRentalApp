package car_rental_app.domain.event;

import java.time.Instant;

public abstract class DomainEvent {
    private final Instant occurredAt = Instant.now();

    /**
     * Timestamp marking when the event was created.
     *
     * @return the Instant representing when this event occurred
     */
    public Instant occurredAt() {
        return occurredAt;
    }

    /**
 * The canonical name of this domain event.
 *
 * @return the event's name used to identify the event
 */
public abstract String eventName();

    /**
     * The topic name used to categorize this domain event.
     *
     * @return the topic name for this event
     */
    public String topic() {
        return eventName();
    }
}