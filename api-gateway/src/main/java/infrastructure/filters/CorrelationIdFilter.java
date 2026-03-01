package infrastructure.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String correlationId = exchange.getRequest().getHeaders().getFirst("X-Correlation-ID");

        if (correlationId == null) {
            correlationId = java.util.UUID.randomUUID().toString();
        }

        exchange.getResponse().getHeaders().add("X-Correlation-ID", correlationId);

        String finalCorrelationId = correlationId;

        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put("correlationId", finalCorrelationId));
    }
}
