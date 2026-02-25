package domain.audit;

import java.time.Instant;
import java.util.List;

public record AuditEvent(
        Instant timestamp,
        String userId,
        List<String> roles,
        String method,
        String path,
        int status,
        long latencyMs,
        String correlationId,
        String traceId,
        String service
) {}
