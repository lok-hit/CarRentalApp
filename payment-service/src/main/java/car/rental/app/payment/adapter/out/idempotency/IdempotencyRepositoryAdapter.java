package car.rental.app.payment.adapter.out.idempotency;

import car.rental.app.payment.domain.port.out.IdempotencyRepository;
import org.springframework.stereotype.Repository;

@Repository
public class IdempotencyRepositoryAdapter implements IdempotencyRepository {

    private final SpringDataIdempotencyRepository repository;

    public IdempotencyRepositoryAdapter(SpringDataIdempotencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean exists(String key) {
        return repository.existsByKey(key);
    }

    @Override
    public void save(String key) {
        repository.save(new IdempotencyKeyDocument(key));
    }
}
