package car.rental.app.payment.adapter.out.idempotency;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataIdempotencyRepository extends MongoRepository<IdempotencyKeyDocument, String> {

    boolean existsByKey(String key);
}
