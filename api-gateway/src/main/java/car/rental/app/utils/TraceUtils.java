package car.rental.app.utils;

import org.springframework.web.server.ServerWebExchange;

public class TraceUtils {

    private TraceUtils() {
    }

    public static String getCorrelationId(ServerWebExchange exchange) {
        return exchange.getRequest()
                .getHeaders()
                .getFirst("X-Correlation-ID");
    }
}
