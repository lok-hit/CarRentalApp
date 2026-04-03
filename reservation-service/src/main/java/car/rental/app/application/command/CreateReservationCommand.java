package car.rental.app.application.command;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateReservationCommand(

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "carId contains invalid characters")
        String carId,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "customerId contains invalid characters")
        String customerId,

        @NotNull
        @FutureOrPresent
        LocalDate from,

        @NotNull
        @Future
        LocalDate to,

        @NotNull
        @Positive
        BigDecimal price,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$", message = "currency must be ISO 4217 (e.g. PLN, EUR, USD)")
        String currency

) {}
