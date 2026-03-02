package saga;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventRepository;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import car_rental_app.domain.saga.event.ReservationRollbackEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(classes = car_rental_app.CarServiceMain.class)
public class FullSagaRollbackFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository repository;

    @Test
    public void shouldProcessFullSagaRollbackFlow() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        // Kafka producer
        var producer = new KafkaProducer<String, String>(Map.of(
                "bootstrap.servers",
                kafka.getBootstrapServers(),
                "key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"));

        // 1)
        ReservationCreatedEvent created = new ReservationCreatedEvent("r1", new CarId("c1"), "u1");

        producer.send(new ProducerRecord<>("reservation-created", mapper.writeValueAsString(created)));

        // 2)
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {

            var pending = repository.findByStatus("PENDING");

            assertThat(pending).isNotEmpty();
            assertThat(pending.stream().anyMatch(e ->
                    e.getEventType().equals("ReservationCreatedEvent"))).isTrue();
        });

        // 3)
        PaymentFailedEvent failed = new PaymentFailedEvent("r1", new CarId("c1"), "u1");
        producer.send(new ProducerRecord<>("payment-failed", mapper.writeValueAsString(failed)));

        // 4)

        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {

            var pending = repository.findByStatus("PENDING");

            assertThat(pending).isNotEmpty();
            assertThat(pending.stream().anyMatch(e ->
                    e.getEventType().equals("ReservationRollbackEvent"))).isTrue();
        });

        // 5)
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("rollbackFlowGroup",
                "true", String.valueOf(kafka));

        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(List.of("reservation-events"));

        // 6)

        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {

            ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer,
                    "reservation-events");

            assertThat(record.value()).contains("ReservationRollbackEvent");
            assertThat(record.value()).contains("r1");
        });
    }
}
