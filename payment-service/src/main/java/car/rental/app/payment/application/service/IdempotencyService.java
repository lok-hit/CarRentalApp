package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.port.out.IdempotencyRepository;
import org.springframework.stereotype.Service;

@Service
public class IdempotencyService {

    private final IdempotencyRepository repository;

    public IdempotencyService(IdempotencyRepository repository) {
        this.repository = repository;
    }

    public boolean isDuplicate(String key) {
        return repository.exists(key);
    }

    public void markProcessed(String key) {
        repository.save(key);
    }
}
