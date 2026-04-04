package car.rental.app.monitoring;

import car.rental.app.monitoring.domain.AuditEvent;
import car.rental.app.monitoring.dto.AuditEventRequest;
import car.rental.app.monitoring.repository.AuditEventRepository;
import car.rental.app.monitoring.service.AuditService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditEventRepository repository;

    private AuditService service;

    @BeforeEach
    void setUp() {
        service = new AuditService(repository, new SimpleMeterRegistry());
    }

    @Test
    void ingest_savesAndReturnsEvent() {
        AuditEventRequest request = new AuditEventRequest(
                "PaymentCompleted", "payment-service",
                "trace-1", "span-1", "corr-1",
                "{}", Instant.now()
        );

        AuditEvent saved = new AuditEvent("id-1", "PaymentCompleted", "payment-service",
                "trace-1", "span-1", "corr-1", "{}", Instant.now(), Instant.now());

        when(repository.save(any())).thenReturn(Mono.just(saved));

        StepVerifier.create(service.ingest(request))
                .assertNext(event -> {
                    assertThat(event.getEventType()).isEqualTo("PaymentCompleted");
                    assertThat(event.getServiceName()).isEqualTo("payment-service");
                    assertThat(event.getTraceId()).isEqualTo("trace-1");
                })
                .verifyComplete();
    }
}
