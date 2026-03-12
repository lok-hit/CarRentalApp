package car.rental.app.domain.port;

public interface EventPublisher {

    void publish(Object event);
}
