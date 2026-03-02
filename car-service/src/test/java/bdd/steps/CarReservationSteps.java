package bdd.steps;


import car_rental_app.application.command.CreateCarCommand;
import car_rental_app.application.service.CarApplicationService;
import car_rental_app.application.service.exception.CarReservationRejectedException;
import car_rental_app.domain.model.CarCategory;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.model.Price;
import io.cucumber.java.en.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CarReservationSteps {

    private final CarApplicationService service;

    private boolean policyAllows;
    private Exception thrown;

    public CarReservationSteps(CarApplicationService service) {
        this.service = service;
    }

    @Given("a car with id {string} exists and can be reserved by policy")
    public void carCanBeReserved(String id) {
        policyAllows = true;
        // ewentualnie: stub/podmiana reservationPolicy w serwisie na taką, która zwraca true
    }

    @Given("a car with id {string} exists and cannot be reserved by policy")
    public void carCannotBeReserved(String id) {
        policyAllows = false;
        // ewentualnie: stub/podmiana reservationPolicy w serwisie na taką, która zwraca false
    }

    @When("a reservation command is handled for car {string}")
    public void handleReservationCommand(String id) {
        try {
            service.handle(new CreateCarCommand(new CarId("d"), CarCategory.SEDAN, new Price(BigDecimal.valueOf(1000)).value()));
        } catch (Exception ex) {
            thrown = ex;
        }
    }

    @Then("the reservation command completes successfully")
    public void reservationSuccess() {
        assertNull(thrown);
    }

    @Then("a CarReservationRejectedException is thrown")
    public void reservationRejected() {
        assertNotNull(thrown);
        assertTrue(thrown instanceof CarReservationRejectedException);
    }
}
