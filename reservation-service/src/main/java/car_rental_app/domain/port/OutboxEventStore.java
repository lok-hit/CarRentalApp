package car_rental_app.domain.port;

public interface OutboxEventStore {

    void save(Object event);
}
