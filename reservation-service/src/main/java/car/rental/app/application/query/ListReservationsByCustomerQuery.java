package car.rental.app.application.query;

import jakarta.validation.constraints.NotBlank;

public record ListReservationsByCustomerQuery(

        @NotBlank(message = "customerId must not be blank")
        String customerId

) {}
