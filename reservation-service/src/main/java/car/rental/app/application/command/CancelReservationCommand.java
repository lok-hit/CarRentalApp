package car.rental.app.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CancelReservationCommand(

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "reservationId contains invalid characters")
        String reservationId,

        @NotBlank
        @Size(min = 3, max = 200, message = "reason must be between 3 and 200 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9 _.,-]+$",
                message = "reason contains invalid characters"
        )
        String reason

) {}
