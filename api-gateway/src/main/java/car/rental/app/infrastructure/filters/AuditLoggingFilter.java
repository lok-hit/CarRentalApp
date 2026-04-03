package car.rental.app.infrastructure.filters;

import car.rental.app.domain.audit.AuditEvent;
import car.rental.app.domain.audit.service.AuditService;
import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
public class AuditLoggingFilter implements WebFilter {
    private final AuditService auditService;

    public AuditLoggingFilter(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long start = System.currentTimeMillis();
        return chain.filter(exchange).then(ReactiveSecurityContextHolder.getContext().flatMap(ctx -> {
            var auth = ctx.getAuthentication();
            String userId = null;
            List<String> roles = List.of();
            if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                userId = jwt.getClaimAsString("sub");
                roles = auth.getAuthorities().stream()
                        .map(a -> a.getAuthority()).toList();
            }
            ServerHttpResponse response = exchange.getResponse();
            AuditEvent event = new AuditEvent(Instant.now(), userId, roles, exchange.getRequest().getMethod()
                    .name(), exchange.getRequest().getPath().value(), response.getStatusCode()
                    != null ? response.getStatusCode().value() : 0,
                    System.currentTimeMillis() - start,
                    MDC.get("correlationId"), MDC.get("trace_id"),
                    "api-gateway");
            return auditService.send(event);
        }).onErrorResume(e -> Mono.empty()));
    }
}
