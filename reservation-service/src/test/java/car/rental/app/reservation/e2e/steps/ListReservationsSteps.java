package car.rental.app.reservation.e2e.steps;

import car.rental.app.reservation.e2e.CucumberSpringConfig;
import io.cucumber.java.en.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class ListReservationsSteps extends CucumberSpringConfig {

    @LocalServerPort
    private int port;

    private ResponseEntity<String> response;

    private final RestTemplate rest = new RestTemplate();

    @Given("user has {int} reservation")
    public void user_has_reservation(Integer count) {
        // insert into Testcontainers Postgres
    }

    @When("user sends GET /reservations")
    public void user_sends_get() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(System.getProperty("TEST_JWT"));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        response = rest.exchange(
                "http://localhost:" + port + "/reservations",
                HttpMethod.GET,
                request,
                String.class
        );
    }

    @Then("response should contain list with size {int}")
    public void response_should_contain_list(Integer size) {
        assertTrue(response.getBody().contains("\"totalElements\":" + size));
    }
}
