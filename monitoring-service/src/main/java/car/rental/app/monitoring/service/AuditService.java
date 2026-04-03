package car.rental.app.monitoring.service;

import car.rental.app.monitoring.domain.AuditEvent;
import car.rental.app.monitoring.dto.AuditEventRequest;
import car.rental.app.monitoring.repository.AuditEventRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final AuditEventRepository repository;
    private final Counter ingestedCounter;

    public AuditService(AuditEventRepository repository, MeterRegistry registry) {
        this.repository = repository;
        this.ingestedCounter = registry.counter("audit.events.ingested");
    }

    public Mono<AuditEvent> ingest(AuditEventRequest request) {
        AuditEvent event = new AuditEvent(
                UUID.randomUUID().toString(),
                request.eventType(),
                request.serviceName(),
                request.traceId(),
                request.spanId(),
                request.correlationId(),
                request.payload(),
                request.occurredAt() != null ? request.occurredAt() : Instant.now(),
                Instant.now()
        );

        log.info("Audit event ingested: type={} service={} traceId={}",
                event.getEventType(), event.getServiceName(), event.getTraceId());

        ingestedCounter.increment();
        return repository.save(event);
    }

    public Flux<AuditEvent> findByTraceId(String traceId) {
        return repository.findByTraceId(traceId);
    }

    public Flux<AuditEvent> findByEventType(String eventType) {
        return repository.findByEventType(eventType);
    }

    public Flux<AuditEvent> findByServiceName(String serviceName) {
        return repository.findByServiceName(serviceName);
    }

    public Flux<AuditEvent> findAll() {
        return repository.findAll();
    }
}
