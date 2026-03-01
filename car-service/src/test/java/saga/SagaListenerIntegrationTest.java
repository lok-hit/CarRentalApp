package saga;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventRepository;
import car_rental_app.adapter.out.persistence.repository.CarMongoRepository;
import car_rental_app.application.service.CarApplicationService;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CarApplicationService.class)
public class SagaListenerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository repository;

    @Test
    void shouldHandleReservationCreatedEventAndWriteToOutbox() throws Exception {
        // given
        var producer = new KafkaProducer<String, String>(
                Map.of("bootstrap.servers", kafka.getBootstrapServers(), "key.serializer",
                        "org.apache.kafka.common.serialization.StringSerializer",
                        "value.serializer",
                        "org.apache.kafka.common.serialization.StringSerializer"));
        ReservationCreatedEvent event = new ReservationCreatedEvent("r1", new CarId("c1"));
        String json = new ObjectMapper().writeValueAsString(event);
        // when
        producer.send(new ProducerRecord<>("reservation-created", json));

        // then
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            var pending = repository.findByStatus("PENDING");

            assertThat(pending).isNotEmpty();
            assertThat(pending.get(0).getEventType()).isEqualTo("ReservationCreatedEvent");
        });
    }
}