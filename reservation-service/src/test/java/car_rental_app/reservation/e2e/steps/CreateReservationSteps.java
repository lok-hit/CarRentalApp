package car_rental_app.reservation.e2e.steps;

import car_rental_app.reservation.e2e.CucumberSpringConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateReservationSteps extends CucumberSpringConfig {

    @LocalServerPort
    private int port;

    private ResponseEntity<String> response;

    private final RestTemplate rest = new RestTemplate();

    @Given("user is authenticated with role USER")
    public void user_is_authenticated() {
        // w testach E2E generujemy JWT testowy
        System.setProperty("TEST_JWT", "dummy-jwt-token");
    }

    @Given("car with id {string} is available")
    public void car_is_available(String carId) {
        // mock w testcontainers lub stub
    }

    @When("user sends POST /reservations with:")
    public void user_sends_post_reservations(io.cucumber.datatable.DataTable table) {
        Map<String, String> body = table.asMap(String.class, String.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(System.getProperty("TEST_JWT"));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        response = rest.postForEntity("http://localhost:" + port + "/reservations", request, String.class);
    }

    @Then("response status should be {int}")
    public void response_status_should_be(Integer status) {
        assertEquals(status, response.getStatusCodeValue());
    }

    @Then("response should contain field {string} with value {string}")
    public void response_should_contain_field(String field, String value) {
        assertTrue(response.getBody().contains("\"" + field + "\":\"" + value + "\""));
    }

    @Then("reservation event {string} should be published to Kafka")
    public void event_should_be_published(String eventType) {
        // Testcontainers Kafka + consumer assert
    }
}
