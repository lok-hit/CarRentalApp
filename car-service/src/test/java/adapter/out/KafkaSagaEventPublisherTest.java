package adapter.out;


import car.rental.app.application.service.saga.client.KafkaSagaEventPublisher;
import car.rental.app.application.service.saga.client.SagaEventPublisher;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.saga.event.ReservationCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class KafkaSagaEventPublisherTest {
    private KafkaTemplate<String, Object> kafka;
    private SagaEventPublisher publisher;
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        kafka = mock(KafkaTemplate.class);
        mapper = new ObjectMapper();
        publisher = new KafkaSagaEventPublisher(kafka);
    }

    @Test
    void shouldPublishEventToKafkaAsJson() throws Exception {
        var event = new ReservationCreatedEvent("r1", new CarId("c1"), "u1");
        publisher.publishReservationCreated(event);
        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);
        verify(kafka).send(eq("reservation-created"), eq("c1"), payloadCaptor.capture());
        Object capturedPayload = payloadCaptor.getValue();
        String json = mapper.writeValueAsString(capturedPayload);
        ReservationCreatedEvent deserialized = mapper.readValue(json, ReservationCreatedEvent.class);
        assertThat(deserialized.reservationId()).isEqualTo("r1");
        assertThat(deserialized.carId().value()).isEqualTo("c1");
    }
}

