package adapter.out;

import car_rental_app.adapter.out.messaging.outbox.OutboxEventDocument;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventPublisher;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OutboxEventPublisherTest {

    private OutboxEventRepository repository;
    private KafkaTemplate<String, String> kafka;
    private OutboxEventPublisher publisher;

    @BeforeEach
    void setup() {
        repository = mock(OutboxEventRepository.class);
        kafka = mock(KafkaTemplate.class);
        publisher = new OutboxEventPublisher(repository, kafka);
    }

    @Test
    void shouldPublishPendingEventsAndMarkAsProcessed() {
        OutboxEventDocument pending = new OutboxEventDocument("123", "r1", "ReservationCreatedEvent", "PROCESSED", Instant.now());
        when(repository.findByStatus("PENDING")).thenReturn(List.of(pending));
        publisher.publishPendingEvents();
        verify(kafka).send("reservation-events", pending.getPayload());
        ArgumentCaptor<OutboxEventDocument> captor = ArgumentCaptor.forClass(OutboxEventDocument.class);
        verify(repository).save(captor.capture());
        OutboxEventDocument saved = captor.getValue();
        assertThat(saved.getAggregateId()).isEqualTo("123");
        assertThat(saved.getEventType()).isEqualTo("r1");
        assertThat(saved.getPayload()).isEqualTo("ReservationCreatedEvent");
        assertThat(saved.getStatus()).isEqualTo("PROCESSED");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldDoNothingWhenNoPendingEvents() {
        when(repository.findByStatus("PENDING")).thenReturn(List.of());
        publisher.publishPendingEvents();
        verifyNoInteractions(kafka);
        verify(repository, never()).save(any());
    }
}

