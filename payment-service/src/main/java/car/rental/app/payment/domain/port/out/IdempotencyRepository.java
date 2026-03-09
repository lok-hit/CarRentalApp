package car.rental.app.payment.domain.port.out;

public interface IdempotencyRepository {

    boolean exists(String key);

    void save(String key);
}