package saga;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.context.SpringBootTest;
import car_rental_app.domain.saga.event.PaymentCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = car_rental_app.CarServiceMain.class)
public class PaymentCompletedIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository outbox;

    @Test
    void shouldHandlePaymentCompletedEventAndPublishConfirmedEventToKafka() throws Exception {
        // given
        var producer = new KafkaProducer<String, String>(Map.of(
                "bootstrap.servers", kafka.getBootstrapServers(),
                "key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"));

        PaymentCompletedEvent event = new PaymentCompletedEvent("r1",
                "c1",
                "u1",
                "CARD_OK");

        String json = new ObjectMapper().writeValueAsString(event);

        // when
        producer.send(new ProducerRecord<>("payment-completed", json));

        // then
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            var pending = outbox.findByStatus("PENDING");

            assertThat(pending).isNotEmpty();
            assertThat(pending.get(0).getEventType()).isEqualTo("ReservationConfirmedEvent");
        });

        // then –
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("confirmGroup", "true", String.valueOf(kafka));
        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(List.of("reservation-events"));
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "reservation-events");
            assertThat(record.value()).contains("ReservationConfirmedEvent");
            assertThat(record.value()).contains("r1");
        });
    }
}


