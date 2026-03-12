package saga;

import car.rental.app.CarServiceMain;
import car.rental.app.BaseIntegrationTest;
import car.rental.app.adapter.out.messaging.outbox.OutboxEventRepository;
import car.rental.app.domain.saga.event.PaymentCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

@SpringBootTest(classes = CarServiceMain.class)
class PaymentCompletedIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository outbox;

    @Test
    void shouldHandlePaymentCompletedEventAndPublishConfirmedEventToKafka() throws Exception {

        System.out.println("Kafka bootstrap servers: " + kafka.getBootstrapServers());

        // given
        var producer = new KafkaProducer<String, String>(Map.of(
                "bootstrap.servers", kafka.getBootstrapServers(),
                "key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer", "org.apache.kafka.common.serialization.StringSerializer"
        ));

        PaymentCompletedEvent event = new PaymentCompletedEvent(
                "r1", "c1", "u1", "CARD_OK"
        );

        String json = new ObjectMapper().writeValueAsString(event);
        System.out.println("Sending JSON: " + json);

        // when
        try {
            producer.send(new ProducerRecord<>("payment-completed", json));
            producer.flush();   // 🔥 ważne
            System.out.println("Message sent to Kafka");
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace();
        }

        // then – outbox
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            var pending = outbox.findByStatus("PENDING");
            System.out.println("Found pending events: " + pending.size());

            assertThat(pending).isNotEmpty();
            assertThat(pending.get(0).getEventType())
                    .isEqualTo("ReservationConfirmedEvent");
        });

        // then – Kafka consumer
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                "confirmGroup",
                "true",
                kafka.getBootstrapServers()   // 🔥 poprawione
        );

        consumerProps.put("auto.offset.reset", "earliest"); // 🔥 konieczne

        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps)
                .createConsumer();

        consumer.subscribe(List.of("reservation-events"));

        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            ConsumerRecord<String, String> recorded =
                    KafkaTestUtils.getSingleRecord(consumer, "reservation-events");

            assertThat(recorded.value()).contains("ReservationConfirmedEvent");
            assertThat(recorded.value()).contains("r1");
        });

        consumer.close();
        producer.close();
    }
}
