package car_rental_app.reservation.e2e.steps;

import car_rental_app.reservation.e2e.CucumberSpringConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CancelReservationSteps extends CucumberSpringConfig {

    @LocalServerPort
    private int port;

    private ResponseEntity<String> response;

    private final RestTemplate rest = new RestTemplate();

    @Given("reservation with id {string} exists")
    public void reservation_exists(String id) {
        // insert into Testcontainers Postgres
    }

    @When("user sends DELETE /reservations/{string}")
    public void user_sends_delete(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(System.getProperty("TEST_JWT"));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        response = rest.exchange(
                "http://localhost:" + port + "/reservations/" + id,
                HttpMethod.DELETE,
                request,
                String.class
        );
    }

    @Then("reservation event {string} should be published to Kafka")
    public void event_published(String eventType) {
        // assert Kafka event
    }
}
