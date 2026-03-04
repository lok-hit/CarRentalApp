package car_rental_app.adapter.in.messaging;


import car_rental_app.application.service.saga.ReservationSagaHandler;
import car_rental_app.domain.event.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CarServiceListener {

    private static final Logger log = LoggerFactory.getLogger(CarServiceListener.class);

    private final ObjectMapper mapper;
    private final ReservationSagaHandler sagaHandler;

    private static final String carId ="carId";

    public CarServiceListener(ObjectMapper mapper, ReservationSagaHandler sagaHandler) {
        this.mapper = mapper;
        this.sagaHandler = sagaHandler;
    }

    @KafkaListener(
            topics = "${kafka.topics.car-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onCarEvent(String message) {
        log.info("Received car-service event: {}", message);

        try {
            JsonNode json = mapper.readTree(message);

            EventValidator.requireText(json, "eventType");
            String type = json.get("eventType").asText();

            switch (type) {

                case "CarCreatedEvent" -> {
                    validateCarCreated(json);
                    CarCreatedEvent event = mapper.treeToValue(json, CarCreatedEvent.class);
                    sagaHandler.handleCarCreated(event);
                }

                case "CarMarkedAsAvailableEvent" -> {
                    validateCarAvailable(json);
                    CarMarkedAsAvailableEvent event = mapper.treeToValue(json, CarMarkedAsAvailableEvent.class);
                    sagaHandler.handleCarAvailable(event);
                }

                case "CarMarkedAsUnavailableEvent" -> {
                    validateCarUnavailable(json);
                    CarMarkedAsUnavailableEvent event = mapper.treeToValue(json, CarMarkedAsUnavailableEvent.class);
                    sagaHandler.handleCarUnavailable(event);
                }

                default -> log.warn("Unknown eventType from car-service: {}", type);
            }

        } catch (Exception e) {
            log.error("Failed to process car-service event: {}", message, e);
        }
    }

    private void validateCarCreated(JsonNode json) {
        car_rental_app.adapter.in.messaging.EventValidator.requireText(json, carId);
        EventValidator.requireText(json, "brand");
        EventValidator.requireText(json, "model");
        EventValidator.requireTimestamp(json, "createdAt");
    }

    private void validateCarAvailable(JsonNode json) {
        EventValidator.requireText(json, carId);
        EventValidator.requireTimestamp(json, "timestamp");
    }

    private void validateCarUnavailable(JsonNode json) {
        EventValidator.requireText(json, carId);
        EventValidator.requireText(json, "reason");
        EventValidator.requireTimestamp(json, "timestamp");
    }
}
