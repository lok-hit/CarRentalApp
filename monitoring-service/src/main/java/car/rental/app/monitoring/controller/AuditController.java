package car.rental.app.monitoring.controller;

import car.rental.app.monitoring.domain.AuditEvent;
import car.rental.app.monitoring.dto.AuditEventRequest;
import car.rental.app.monitoring.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping("/events")
    public Mono<ResponseEntity<AuditEvent>> ingest(@RequestBody AuditEventRequest request) {
        return auditService.ingest(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/events")
    public Flux<AuditEvent> findAll() {
        return auditService.findAll();
    }

    @GetMapping("/events/trace/{traceId}")
    public Flux<AuditEvent> findByTraceId(@PathVariable String traceId) {
        return auditService.findByTraceId(traceId);
    }

    @GetMapping("/events/type/{eventType}")
    public Flux<AuditEvent> findByEventType(@PathVariable String eventType) {
        return auditService.findByEventType(eventType);
    }

    @GetMapping("/events/service/{serviceName}")
    public Flux<AuditEvent> findByServiceName(@PathVariable String serviceName) {
        return auditService.findByServiceName(serviceName);
    }
}
