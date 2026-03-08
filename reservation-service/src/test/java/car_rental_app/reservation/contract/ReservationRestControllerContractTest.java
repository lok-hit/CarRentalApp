package car_rental_app.reservation.contract;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationRestControllerContractTest extends BaseContractTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void shouldCreateReservation() {
        String url = "http://localhost:" + port + "/reservations";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
            {
              "carId": "car-123",
              "startDate": "2026-03-10T10:00:00Z",
              "endDate": "2026-03-12T10:00:00Z",
              "notes": "Child seat"
            }
        """;

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("\"status\":\"CONFIRMED\""));
    }
}

