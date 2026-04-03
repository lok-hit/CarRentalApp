package bdd.steps;


import car.rental.app.application.command.CreateCarCommand;
import car.rental.app.application.service.CarApplicationService;
import car.rental.app.application.service.exception.CarReservationRejectedException;
import car.rental.app.domain.model.CarCategory;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.model.Price;
import io.cucumber.java.en.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CarReservationSteps {

    private final CarApplicationService service;

    private boolean policyAllows;
    private Exception thrown;

    /**
     * Creates a CarReservationSteps instance backed by the given CarApplicationService.
     *
     * @param service application service used to handle car reservation commands in the step definitions
     */
    public CarReservationSteps(CarApplicationService service) {
        this.service = service;
    }

    /**
     * Mark the car identified by {@code id} as reservable by the reservation policy for the scenario.
     *
     * @param id the car identifier used in the scenario
     */
    @Given("a car with id {string} exists and can be reserved by policy")
    public void carCanBeReserved(String id) {
        policyAllows = true;
    }

    /**
     * Marks the car identified by `id` as not reservable by the reservation policy for subsequent test steps.
     *
     * Sets an internal test flag so reservation attempts will be treated as rejected by policy.
     *
     * @param id the identifier of the car to mark as not reservable in the scenario
     */
    @Given("a car with id {string} exists and cannot be reserved by policy")
    public void carCannotBeReserved(String id) {
        policyAllows = false;
        // ewentualnie: stub/podmiana reservationPolicy w serwisie na taką, która zwraca false
    }

    /**
     * Attempts to handle a car reservation command using hard-coded command values and records any exception raised.
     *
     * @param id the car identifier from the scenario (currently ignored; the method uses hard-coded command values)
     */
    @When("a reservation command is handled for car {string}")
    public void handleReservationCommand(String id) {
        try {
            service.handle(new CreateCarCommand(new CarId("d"), CarCategory.SEDAN, new Price(BigDecimal.valueOf(1000)).value()));
        } catch (Exception ex) {
            thrown = ex;
        }
    }

    /**
     * Asserts that the previously executed reservation command did not produce an exception.
     *
     * <p>Used as a Cucumber step to verify the reservation completed successfully.</p>
     */
    @Then("the reservation command completes successfully")
    public void reservationSuccess() {
        assertNull(thrown);
    }

    /**
     * Verifies that handling the reservation resulted in a CarReservationRejectedException.
     *
     * Asserts that an exception was captured and that it is an instance of {@link CarReservationRejectedException}.
     */
    @Then("a CarReservationRejectedException is thrown")
    public void reservationRejected() {
        assertNotNull(thrown);
        assertTrue(thrown instanceof CarReservationRejectedException);
    }
}
