package car.rental.app.monitoring.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "audit_events")
public class AuditEvent {

    @Id
    private String id;
    private String eventType;
    private String serviceName;
    private String traceId;
    private String spanId;
    private String correlationId;
    private String payload;
    private Instant occurredAt;
    private Instant receivedAt;

    public AuditEvent() {}

    public AuditEvent(String id, String eventType, String serviceName,
                      String traceId, String spanId, String correlationId,
                      String payload, Instant occurredAt, Instant receivedAt) {
        this.id = id;
        this.eventType = eventType;
        this.serviceName = serviceName;
        this.traceId = traceId;
        this.spanId = spanId;
        this.correlationId = correlationId;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.receivedAt = receivedAt;
    }

    public String getId() { return id; }
    public String getEventType() { return eventType; }
    public String getServiceName() { return serviceName; }
    public String getTraceId() { return traceId; }
    public String getSpanId() { return spanId; }
    public String getCorrelationId() { return correlationId; }
    public String getPayload() { return payload; }
    public Instant getOccurredAt() { return occurredAt; }
    public Instant getReceivedAt() { return receivedAt; }

    public void setId(String id) { this.id = id; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public void setSpanId(String spanId) { this.spanId = spanId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
    public void setReceivedAt(Instant receivedAt) { this.receivedAt = receivedAt; }
}
