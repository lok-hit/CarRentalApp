package car_rental_app.domain.port;

public interface EventPublisher {

    void publish(Object event);
}
