import domain.events.ApiGatewayEventPublisher;
import infrastructure.filters.CorrelationIdFilter;
import infrastructure.filters.LoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


public class LoggingFilterTest {

    private ApiGatewayEventPublisher eventPublisher;
    private LoggingFilter loggingFilter;
    private GatewayFilterChain filterChain;

    @BeforeEach
    public void setup() {
        eventPublisher = mock(ApiGatewayEventPublisher.class);
        loggingFilter = new LoggingFilter(eventPublisher);
        filterChain = mock(GatewayFilterChain.class);

        doReturn(Mono.empty()).when(filterChain).filter(any());

    }

    @Test
    void shouldPublishEventWithUserIdAndCorrelationId() {
        // given
        String correlationId = "corr-123";

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/cars/available")
                .header("X-Correlation-ID", correlationId)
                .build();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user-123")
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);
        ServerWebExchange exchange = MockServerWebExchange
                .from(request)
                .mutate()
                .principal(Mono.just(auth))
                .build();

        // when
        Mono<Void> result = loggingFilter.filter(exchange, filterChain);

        // then
        StepVerifier.create(result).verifyComplete();

        verify(eventPublisher, times(1))
                .publishRequestEvent("/cars/available", "user-123", "GET", correlationId);
    }

    @Test
    void shouldUseAnonymousWhenNoPrincipal() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/users/me")
                .header("X-Correlation-ID", "cid-999")
                .build();

        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // when
        Mono<Void> result = loggingFilter.filter(exchange, filterChain);

        // then
        StepVerifier.create(result).verifyComplete();

        verify(eventPublisher, times(1))
                .publishRequestEvent("/users/me", "anonymous", "GET", "cid-999");
    }



    @Test
    void shouldGenerateCorrelationIdWhenMissing() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/bff/dashboard")
                .build();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user-xyz")
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);
        ServerWebExchange exchange = MockServerWebExchange
                .from(request)
                .mutate()
                .principal(Mono.just(auth))
                .build();

        new CorrelationIdFilter().filter(exchange, filterChain).block();
        ArgumentCaptor<String> correlationCaptor = ArgumentCaptor.forClass(String.class);

        // when
        Mono<Void> result = loggingFilter.filter(exchange, filterChain);

        // then
        StepVerifier.create(result).verifyComplete();

        verify(eventPublisher).publishRequestEvent(
                eq("/bff/dashboard"),
                eq("user-xyz"),
                eq("GET"),
                correlationCaptor.capture()
        );

        String generatedCorrelationId = correlationCaptor.getValue();
        assert generatedCorrelationId != null && !generatedCorrelationId.isBlank();
    }

    @Test
    void shouldPropagateErrorWhenEventPublisherFails() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/cars/available")
                .header("X-Correlation-ID", "cid-123")
                .build();

        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user-123")
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);

        exchange.getAttributes().put(
                "org.springframework.security.core.context.SecurityContext",
                Mono.just(auth)
        );

        doThrow(new RuntimeException("Event failure"))
                .when(eventPublisher)
                .publishRequestEvent(any(), any(), any(), any());

        // when
        Mono<Void> result = loggingFilter.filter(exchange, filterChain);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Event failure"))
                .verify();
    }
}
