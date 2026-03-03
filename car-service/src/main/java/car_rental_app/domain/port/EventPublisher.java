package car_rental_app.domain.port;

public interface EventPublisher { /**
 * Publishes an event to the specified topic.
 *
 * @param topic the topic or channel to which the event will be emitted
 */
void publish(String topic); }
