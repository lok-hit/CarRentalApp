package car.rental.app.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CancelReservationRequest(

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9 _.,-]{3,100}$",
                message = "reason contains invalid characters")
        String reason
) {}
