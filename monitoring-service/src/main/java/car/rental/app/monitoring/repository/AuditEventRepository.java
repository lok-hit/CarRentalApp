package car.rental.app.monitoring.repository;

import car.rental.app.monitoring.domain.AuditEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AuditEventRepository extends ReactiveMongoRepository<AuditEvent, String> {

    Flux<AuditEvent> findByEventType(String eventType);

    Flux<AuditEvent> findByServiceName(String serviceName);

    Flux<AuditEvent> findByTraceId(String traceId);
}
