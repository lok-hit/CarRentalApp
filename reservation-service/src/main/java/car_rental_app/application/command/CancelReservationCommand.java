package car_rental_app.application.command;

import jakarta.validation.constraints.NotBlank;

public record CancelReservationCommand(

        @NotBlank(message = "reservationId must not be blank")
        String reservationId,

        String reason

) {}
