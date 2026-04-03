package unit;

import car.rental.app.domain.audit.AuditEvent;
import car.rental.app.domain.audit.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuditServiceTest {

    private WebClient monitoringWebClient;
    private RequestBodyUriSpec requestBodyUriSpec;
    private RequestHeadersSpec requestHeadersSpec;
    private ResponseSpec responseSpec;
    private AuditService auditService;

    @BeforeEach
    void setup() {
        monitoringWebClient = Mockito.mock(WebClient.class);
        requestBodyUriSpec = Mockito.mock(RequestBodyUriSpec.class);
        requestHeadersSpec = Mockito.mock(RequestHeadersSpec.class);
        responseSpec = Mockito.mock(ResponseSpec.class);

        auditService = new AuditService(monitoringWebClient);

        when(monitoringWebClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void shouldSendAuditEventSuccessfully() {
        AuditEvent event = new AuditEvent(
                Instant.now(),
                "user123",
                List.of("ROLE_USER", "ROLE_ADMIN"),
                "GET",
                "/api/cars",
                200,
                150,
                "corr-123",
                "trace-456",
                "api-gateway"
        );

        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        Mono<Void> result = auditService.send(event);

        StepVerifier.create(result).verifyComplete();

        verify(monitoringWebClient).post();
        verify(requestBodyUriSpec).uri("/audit");
        verify(requestBodyUriSpec).bodyValue(event);
        verify(responseSpec).bodyToMono(Void.class);
    }

    @Test
    void shouldHandleWebClientErrorGracefully() {
        AuditEvent event = new AuditEvent(
                Instant.now(),
                "user123",
                List.of("ROLE_USER"),
                "POST",
                "/api/reservations",
                201,
                200,
                "corr-789",
                "trace-012",
                "api-gateway"
        );

        when(responseSpec.bodyToMono(Void.class))
                .thenReturn(Mono.error(new RuntimeException("Network error")));

        Mono<Void> result = auditService.send(event);

        StepVerifier.create(result).verifyComplete();

        verify(monitoringWebClient).post();
        verify(requestBodyUriSpec).uri("/audit");
        verify(requestBodyUriSpec).bodyValue(event);
        verify(responseSpec).bodyToMono(Void.class);
    }

    @Test
    void shouldHandleBodyToMonoError() {
        AuditEvent event = new AuditEvent(
                Instant.now(),
                "user456",
                List.of("ROLE_ADMIN"),
                "PUT",
                "/api/cars/123",
                400,
                50,
                "corr-345",
                "trace-678",
                "api-gateway"
        );

        when(responseSpec.bodyToMono(Void.class))
                .thenReturn(Mono.error(new RuntimeException("Body processing error")));

        Mono<Void> result = auditService.send(event);

        StepVerifier.create(result).verifyComplete();

        verify(monitoringWebClient).post();
        verify(requestBodyUriSpec).uri("/audit");
        verify(requestBodyUriSpec).bodyValue(event);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(Void.class);
    }

    @Test
    void shouldHandleBodyValueError() {
        AuditEvent event = new AuditEvent(
                Instant.now(),
                "user789",
                List.of("ROLE_USER"),
                "DELETE",
                "/api/reservations/456",
                204,
                75,
                "corr-901",
                "trace-234",
                "api-gateway"
        );

        when(requestBodyUriSpec.bodyValue(any(Object.class)))
                .thenThrow(new RuntimeException("Serialization error"));
        
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        Mono<Void> result = auditService.send(event);

        StepVerifier.create(result).verifyComplete();

        verify(monitoringWebClient).post();
        verify(requestBodyUriSpec).uri("/audit");
        verify(requestBodyUriSpec).bodyValue(event);
    }

    @Test
    void shouldHandleRetrieveError() {
        AuditEvent event = new AuditEvent(
                Instant.now(),
                "user000",
                List.of("ROLE_GUEST"),
                "GET",
                "/api/health",
                503,
                1000,
                "corr-567",
                "trace-890",
                "api-gateway"
        );

        when(requestBodyUriSpec.retrieve())
                .thenThrow(new RuntimeException("Connection refused"));
        
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        Mono<Void> result = auditService.send(event);

        StepVerifier.create(result).verifyComplete();

        verify(monitoringWebClient).post();
        verify(requestBodyUriSpec).uri("/audit");
        verify(requestBodyUriSpec).bodyValue(event);
        verify(requestHeadersSpec).retrieve();
    }

    @Test
    void shouldSendMultipleAuditEventsSequentially() {
        AuditEvent event1 = new AuditEvent(
                Instant.now(),
                "user111",
                List.of("ROLE_USER"),
                "GET",
                "/api/cars",
                200,
                100,
                "corr-111",
                "trace-111",
                "api-gateway"
        );

        AuditEvent event2 = new AuditEvent(
                Instant.now(),
                "user222",
                List.of("ROLE_ADMIN"),
                "POST",
                "/api/cars",
                201,
                150,
                "corr-222",
                "trace-222",
                "api-gateway"
        );

        when(responseSpec.bodyToMono(Void.class))
                .thenReturn(Mono.empty())
                .thenReturn(Mono.empty());

        Mono<Void> result1 = auditService.send(event1);
        Mono<Void> result2 = auditService.send(event2);

        StepVerifier.create(result1).verifyComplete();
        StepVerifier.create(result2).verifyComplete();

        verify(monitoringWebClient, times(2)).post();
        verify(requestBodyUriSpec, times(2)).uri("/audit");
        verify(requestBodyUriSpec, times(2)).bodyValue(any(AuditEvent.class));
        verify(responseSpec, times(2)).bodyToMono(Void.class);
    }

    @Test
    void shouldHandleNullAuditEvent() {
        when(requestBodyUriSpec.bodyValue(any(Object.class)))
                .thenThrow(new IllegalArgumentException("Event cannot be null"));
        
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        Mono<Void> result = auditService.send(null);

        StepVerifier.create(result).verifyComplete();

        verify(monitoringWebClient).post();
        verify(requestBodyUriSpec).uri("/audit");
        verify(requestBodyUriSpec).bodyValue(null);
    }
}
