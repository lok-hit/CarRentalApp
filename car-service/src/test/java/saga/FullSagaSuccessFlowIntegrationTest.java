package saga;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventRepository;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.saga.event.ReservationConfirmedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import car_rental_app.domain.saga.event.PaymentCompletedEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = car_rental_app.CarServiceMain.class)
public class FullSagaSuccessFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository outbox;

    @Test
    public void shouldProcessFullSagaSuccessFlow() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var producer = new KafkaProducer<String, String>(Map.of(
                "bootstrap.servers",
                kafka.getBootstrapServers(),
                "key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"));

        // 1)
        ReservationCreatedEvent created = new ReservationCreatedEvent("r1", new CarId("c1"), "8");
        producer.send(new ProducerRecord<>("reservation-created", mapper.writeValueAsString(created)));

        // 2)
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {

            var pending = outbox.findByStatus("PENDING");

            assertThat(pending).isNotEmpty();
            assertThat(pending.get(0).getEventType()).isEqualTo("ReservationCreatedEvent");
        });

        // 3)
        PaymentCompletedEvent payment = new PaymentCompletedEvent("r1", "c1",
                "u1", "CARD_OK");

        producer.send(new ProducerRecord<>("payment-completed", mapper.writeValueAsString(payment)));

        // 4)
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            var pending = outbox.findByStatus("PENDING");

            assertThat(pending).isNotEmpty();
            assertThat(pending.stream().anyMatch(e ->
                    e.getEventType().equals("ReservationConfirmedEvent"))).isTrue();
        });

        // 5)

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("fullSagaGroup", "true",
                String.valueOf(kafka));

        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(List.of("reservation-events"));

        // 6)

        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {

            ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer,
                    "reservation-events");

            assertThat(record.value()).contains("ReservationConfirmedEvent");
            assertThat(record.value()).contains("r1");
        });
    }
}

