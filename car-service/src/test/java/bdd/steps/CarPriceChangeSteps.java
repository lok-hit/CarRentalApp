package bdd.steps;

import car_rental_app.application.command.ChangeCarPriceCommand;
import car_rental_app.application.port.CarCommandPort;
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

    @Given("a ChangeCarPriceCommand body with id {string} and new price {string}")
    public void givenCommand(String id, String price) {
        requestBody = """
{
  "id": "%s",
  "newPrice": %s
}
""".formatted(id, price);
    }

    @When("PUT {string} is executed with this body")
    public void executePut(String path) throws Exception {
        result = mockMvc.perform(put(path).contentType("application/json").content(requestBody)).andReturn();
    }

    @Then("the response status is {int}")
    public void verifyStatus(int expectedStatus) {
        assertEquals(expectedStatus, result.getResponse().getStatus());
    }

    @Then("the application service is not invoked")
    public void verifyServiceNotInvoked() {
        verify(commandPort, never()).handle(any(ChangeCarPriceCommand.class));
    }
}