package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.port.out.IdempotencyRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {

    private final IdempotencyRepository repository;
    private final Set<String> executedKeys = ConcurrentHashMap.newKeySet();

    public IdempotencyService(IdempotencyRepository repository) {
        this.repository = repository;
    }

    public boolean isDuplicate(String key) {
        return repository.exists(key);
    }

    public void markProcessed(String key) {
        repository.save(key);
    }

    public boolean tryExecute(String key) {
        return executedKeys.add(key);

    }
}