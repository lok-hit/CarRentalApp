package car.rental.app.infrastructure.error;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import car.rental.app.utils.TraceUtils;

import java.nio.charset.StandardCharsets;

public class RateLimitErrorHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof RequestNotPermitted) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            String body = """
                    {
                      "error": "too_many_requests",
                      "message": "Rate limit exceeded",
                      "correlationId": "%s"
                    }
                    """.formatted(TraceUtils.getCorrelationId(exchange));

            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(body.getBytes(StandardCharsets.UTF_8));

            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        return Mono.error(ex);
    }


}
