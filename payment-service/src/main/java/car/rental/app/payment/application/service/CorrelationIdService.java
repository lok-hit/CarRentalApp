package car.rental.app.payment.application.service;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CorrelationIdService {

    private static final String TRACE_ID = "traceId";
    private static final String CORRELATION_ID = "correlationId";

    public String getOrCreateTraceId() {
        return MDC.get(TRACE_ID) != null ? MDC.get(TRACE_ID) : UUID.randomUUID().toString();
    }

    public String getOrCreateCorrelationId() {
        return MDC.get(CORRELATION_ID) != null ? MDC.get(CORRELATION_ID) : UUID.randomUUID().toString();
    }
}