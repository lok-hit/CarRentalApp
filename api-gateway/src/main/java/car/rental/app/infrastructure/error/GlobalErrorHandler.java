package car.rental.app.infrastructure.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Gateway error for path {}: {} - {}", exchange.getRequest().getPath().value(),
                ex.getClass().getSimpleName(), ex.getMessage(), ex);

        HttpStatusCode status = resolveStatus(ex);
        String path = exchange.getRequest().getPath().value();
        String correlationId = exchange.getRequest().getHeaders().getFirst("X-Correlation-ID");

        String type = resolveType(ex, status);

        Map<String, Object> body = new HashMap<>();
        body.put("type", type);
        body.put("title", status.value());
        body.put("status", status.value());
        body.put("detail", ex.getMessage() != null ? ex.getMessage() : "Unexpected error");
        body.put("instance", path);
        body.put("correlationId", correlationId);
        body.put("timestamp", Instant.now().toString());

        byte[] bytes = toJson(body);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private HttpStatusCode resolveStatus(Throwable ex) {

        if (ex instanceof ResponseStatusException rse) {
            return rse.getStatusCode();
        }

        // Rate limiting (RedisRateLimiter / RequestNotPermitted)
        if (ex.getClass().getSimpleName().equals("RequestNotPermitted")) {
            return HttpStatus.TOO_MANY_REQUESTS;
        }

        // Circuit Breaker open
        if (ex.getClass().getSimpleName().equals("CallNotPermittedException")) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveType(Throwable ex, HttpStatusCode status) {

        if (ex.getClass().getSimpleName().equals("RequestNotPermitted")) {
            return "https://api.example.com/errors/rate-limit-exceeded";
        }

        if (ex.getClass().getSimpleName().equals("CallNotPermittedException")) {
            return "https://api.example.com/errors/circuit-breaker-open";
        }

        if (status.is4xxClientError()) {
            return "https://api.example.com/errors/client-error";
        }

        if (status.is5xxServerError()) {
            return "https://api.example.com/errors/server-error";
        }

        return "https://api.example.com/errors/gateway-error";
    }

    private byte[] toJson(Object value) {
        try {
            return MAPPER.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            return "{}".getBytes(StandardCharsets.UTF_8);
        }
    }
}