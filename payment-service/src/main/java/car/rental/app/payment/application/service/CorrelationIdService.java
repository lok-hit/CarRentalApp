package car.rental.app.payment.application.service;

import org.springframework.stereotype.Service;

@Service
public class CorrelationIdService {

    private static final ThreadLocal<String> correlationIdHolder = new ThreadLocal<>();

    public void bind(String correlationId) {
        correlationIdHolder.set(correlationId);
    }

    public String current() {
        return correlationIdHolder.get();
    }

    public void clear() {
        correlationIdHolder.remove();
    }

    public String getOrCreateTraceId() {
        String id = correlationIdHolder.get();
        return id != null ? id : java.util.UUID.randomUUID().toString();
    }

    public String getOrCreateCorrelationId() {
        return getOrCreateTraceId();
    }
}
