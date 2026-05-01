package car.rental.app.application.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers, ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class, ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class));
    }

    /**
     * Create a KafkaTemplate for sending messages with String keys and Object values.
     *
     * @return a KafkaTemplate configured to send messages with String keys and Object values
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Creates a Kafka ProducerFactory configured to connect to localhost:9092 using String serialization for keys and values.
     *
     * @return a ProducerFactory that produces String-keyed, String-valued Kafka producers configured with bootstrap server localhost:9092
     */
    @Bean
    public ProducerFactory<String, String> stringProducerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers, ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class, ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class));
    }

    /**
     * Creates a KafkaTemplate for sending messages with String keys and String values.
     *
     * @return the KafkaTemplate configured for String keys and String values
     */
    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {
        return new KafkaTemplate<>(stringProducerFactory());
    }

    /**
     * Create a Kafka consumer factory preconfigured for the "car-service" group and localhost:9092 bootstrap server.
     *
     * @return a ConsumerFactory that produces consumers using String key and value deserializers, group id "car-service",
     *         bootstrap server "localhost:9092", and auto offset reset set to "earliest"
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG, "car-service",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        ));
    }

    /**
     * Creates a ConcurrentKafkaListenerContainerFactory configured to use the consumerFactory defined in this configuration.
     *
     * @return a ConcurrentKafkaListenerContainerFactory wired to the local consumerFactory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /**
     * Create a ConsumerFactory configured to consume JSON-serialized message values with String keys.
     *
     * @return a ConsumerFactory<String, Object> using StringDeserializer for keys, JsonDeserializer for values,
     *         connecting to localhost:9092, using group id "car-service", auto offset reset "earliest",
     *         and configured to trust all packages for JSON deserialization.
     */
    @Bean
    public ConsumerFactory<String, Object> jsonConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG, "car-service",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
            JsonDeserializer.TRUSTED_PACKAGES, "*"
        ));
    }

    /**
     * Creates a listener container factory configured to consume messages with String keys and JSON-deserialized Object values.
     *
     * @return a ConcurrentKafkaListenerContainerFactory that produces listener containers for String-keyed, JSON-deserialized Object messages
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> jsonKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(jsonConsumerFactory());
        return factory;
    }
}
