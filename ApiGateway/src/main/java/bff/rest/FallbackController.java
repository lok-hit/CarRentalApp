package bff.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping("/fallback/cars")
    public Mono<Map<String, Object>> carsFallback(ServerWebExchange exchange) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "cars_service_unavailable");
        body.put("message", "Cars service is temporarily unavailable");
        body.put("timestamp", Instant.now().toString());
        body.put("correlationId", exchange.getRequest().getHeaders().getFirst("X-Correlation-ID"));
        return Mono.just(body);
    }
}
