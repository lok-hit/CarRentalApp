package utils;

import org.springframework.web.server.ServerWebExchange;

public class TraceUtils {

    public static String getCorrelationId(ServerWebExchange exchange) {
        return exchange.getRequest()
                .getHeaders()
                .getFirst("X-Correlation-ID");
    }
}
