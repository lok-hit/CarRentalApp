package car_rental_app.adapter.in.messaging;

import com.fasterxml.jackson.databind.JsonNode;

public class EventValidator {

    private EventValidator(){


    }
    public static void require(JsonNode json, String field) {
        if (!json.has(field) || json.get(field).isNull()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
    }

    public static void requireText(JsonNode json, String field) {
        require(json, field);
        if (!json.get(field).isTextual()) {
            throw new IllegalArgumentException("Field must be text: " + field);
        }
    }

    public static void requireTimestamp(JsonNode json, String field) {
        require(json, field);
        if (!json.get(field).isTextual()) {
            throw new IllegalArgumentException("Timestamp must be ISO string: " + field);
        }
    }
}
