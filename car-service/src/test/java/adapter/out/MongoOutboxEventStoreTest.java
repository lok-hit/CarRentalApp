package adapter.out;

import car.rental.app.adapter.out.messaging.outbox.MongoOutboxEventStore;
import car.rental.app.adapter.out.messaging.outbox.OutboxEventDocument;
import car.rental.app.adapter.out.messaging.outbox.OutboxEventRepository;
import car.rental.app.domain.event.CarCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MongoOutboxEventStoreTest {
    private OutboxEventRepository repository;
    private MongoOutboxEventStore store;

    @BeforeEach
    void setup() {
        repository = mock(OutboxEventRepository.class);
        store = new MongoOutboxEventStore(repository);
    }

    @Test
    void saveEventShouldStoreEventInMongoWithPendingStatus() {
        var event = new CarCreatedEvent("c1", "SEDAN", BigDecimal.valueOf(1000));
        store.saveEvent("c1", event);
        ArgumentCaptor<OutboxEventDocument> captor = ArgumentCaptor.forClass(OutboxEventDocument.class);
        verify(repository).save(captor.capture());
        OutboxEventDocument saved = captor.getValue();
        assertThat(saved.getAggregateId()).isEqualTo("c1");
        assertThat(saved.getEventType()).isEqualTo(event.getClass().getSimpleName());
        assertThat(saved.getPayload()).isNotBlank();
        assertThat(saved.getCreatedAt()).isNotNull();
    }
}