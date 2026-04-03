package car.rental.app.domain.port;

public interface OutboxEventStore {

    void save(Object event);
}
