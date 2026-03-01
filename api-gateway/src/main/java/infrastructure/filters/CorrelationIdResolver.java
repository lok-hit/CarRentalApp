package infrastructure.filters;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component("correlationIdResolver")
public class CorrelationIdResolver {

    public String resolve() {
        return MDC.get("correlationId");
    }
}

