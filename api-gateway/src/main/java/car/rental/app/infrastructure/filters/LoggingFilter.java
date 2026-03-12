package car.rental.app.infrastructure.filters;

import car.rental.app.domain.events.ApiGatewayEventPublisher;
import car.rental.app.utils.TraceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    private final ApiGatewayEventPublisher eventPublisher;

    public LoggingFilter(ApiGatewayEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        String method = request.getMethod().name();
        String correlationId = TraceUtils.getCorrelationId(exchange);

        // Log wejściowy
        log.info("Incoming request: method={}, path={}, correlationId={}",
                method, path, correlationId);

        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .map(jwt -> jwt.getToken().getSubject()) // userId = sub
                .defaultIfEmpty("anonymous")
                .doOnNext(userId -> {
                    // Publikacja eventu CloudEvent
                    eventPublisher.publishRequestEvent(
                            path,
                            userId,
                            method,
                            correlationId
                    );

                    // Log strukturalny
                    log.info("Request event published: userId={}, path={}, method={}, correlationId={}",
                            userId, path, method, correlationId);
                })
                .then(chain.filter(exchange));
    }
}

