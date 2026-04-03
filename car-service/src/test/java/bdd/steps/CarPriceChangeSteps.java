package bdd.steps;

import car.rental.app.application.command.ChangeCarPriceCommand;
import car.rental.app.application.port.CarCommandPort;
import io.cucumber.java.en.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class CarPriceChangeSteps {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarCommandPort commandPort;
    private String requestBody;
    private MvcResult result;

    /**
     * Builds the JSON request body for a ChangeCarPriceCommand using the provided car id and price.
     *
     * @param id    the car identifier to include as the JSON "id" field
     * @param price the new price to include as the JSON "newPrice" field; inserted as a numeric value
     */
    @Given("a ChangeCarPriceCommand body with id {string} and new price {string}")
    public void givenCommand(String id, String price) {
        requestBody = """
{
  "id": "%s",
  "newPrice": %s
}
""".formatted(id, price);
    }

    /**
     * Performs an HTTP PUT to the given endpoint path using the prepared JSON request body and stores the MVC result.
     *
     * @param path the endpoint path to send the PUT request to
     * @throws Exception if executing the request fails
     */
    @When("PUT {string} is executed with this body")
    public void executePut(String path) throws Exception {
        result = mockMvc.perform(put(path).contentType("application/json").content(requestBody)).andReturn();
    }

    /**
     * Asserts that the HTTP response status equals the given expected HTTP status code.
     *
     * @param expectedStatus the expected HTTP status code
     */
    @Then("the response status is {int}")
    public void verifyStatus(int expectedStatus) {
        assertEquals(expectedStatus, result.getResponse().getStatus());
    }

    /**
     * Asserts that the application service port was not invoked with a ChangeCarPriceCommand.
     *
     * Verifies that no call was made to handle a ChangeCarPriceCommand during the test.
     */
    @Then("the application service is not invoked")
    public void verifyServiceNotInvoked() {
        verify(commandPort, never()).handle(any(ChangeCarPriceCommand.class));
    }
}