package car.rental.app.application.query;

import jakarta.validation.constraints.NotBlank;

public record GetReservationQuery(

        @NotBlank(message = "reservationId must not be blank")
        String reservationId

) {}
