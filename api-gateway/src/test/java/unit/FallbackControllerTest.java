package unit;

import car_rental_app.bff.rest.FallbackController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Map;

class FallbackControllerTest {

    private FallbackController controller;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        controller = new FallbackController();
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldReturnCarsFallbackResponse() {
        // when & then
        webTestClient.get()
                .uri("/fallback/cars")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.error").isEqualTo("cars_service_unavailable")
                .jsonPath("$.message").isEqualTo("Cars service is temporarily unavailable")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.correlationId").doesNotExist();
    }

    @Test
    void shouldReturnCarsFallbackWithCorrelationId() {
        // when & then
        webTestClient.get()
                .uri("/fallback/cars")
                .header("X-Correlation-ID", "test-correlation-123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.error").isEqualTo("cars_service_unavailable")
                .jsonPath("$.message").isEqualTo("Cars service is temporarily unavailable")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.correlationId").isEqualTo("test-correlation-123");
    }

    @Test
    void shouldVerifyCarsFallbackMethodReturnsCorrectMono() {
        // given
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/fallback/cars").build()
        );

        // when
        Mono<Map<String, Object>> result = controller.carsFallback(exchange);

        // then
        StepVerifier.create(result)
                .expectNextMatches(body -> body.containsKey("error") &&
                       body.containsKey("message") &&
                       body.containsKey("timestamp") &&
                       body.get("error").equals("cars_service_unavailable") &&
                       body.get("message").equals("Cars service is temporarily unavailable") &&
                       body.get("timestamp") instanceof String)
                .verifyComplete();
    }

    @Test
    void shouldVerifyCarsFallbackMethodWithCorrelationId() {
        // given
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/fallback/cars")
                        .header("X-Correlation-ID", "test-correlation-456")
                        .build()
        );

        // when
        Mono<Map<String, Object>> result = controller.carsFallback(exchange);

        // then
        StepVerifier.create(result)
                .expectNextMatches(body -> body.containsKey("error") &&
                       body.containsKey("message") &&
                       body.containsKey("timestamp") &&
                       body.containsKey("correlationId") &&
                       body.get("error").equals("cars_service_unavailable") &&
                       body.get("message").equals("Cars service is temporarily unavailable") &&
                       body.get("correlationId").equals("test-correlation-456") &&
                       body.get("timestamp") instanceof String)
                .verifyComplete();
    }

    @Test
    void shouldVerifyTimestampFormat() {
        // given
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/fallback/cars").build()
        );

        // when
        Mono<Map<String, Object>> result = controller.carsFallback(exchange);

        // then
        StepVerifier.create(result)
                .expectNextMatches(body -> {
                    String timestamp = (String) body.get("timestamp");
                    try {
                        Instant.parse(timestamp);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleNullCorrelationId() {
        // given
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/fallback/cars").build()
        );

        // when
        Mono<Map<String, Object>> result = controller.carsFallback(exchange);

        // then
        StepVerifier.create(result)
                .expectNextMatches(body -> body.containsKey("correlationId") &&
                       body.get("correlationId") == null)
                .verifyComplete();
    }
}
