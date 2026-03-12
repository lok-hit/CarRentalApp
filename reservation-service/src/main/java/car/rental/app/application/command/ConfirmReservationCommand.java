package car.rental.app.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ConfirmReservationCommand(

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "reservationId contains invalid characters")
        String reservationId

) {}
