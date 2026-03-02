package car_rental_app.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateReservationCommand(

        @NotBlank(message = "carId must not be blank")
        String carId,

        @NotBlank(message = "customerId must not be blank")
        String customerId,

        @NotNull(message = "from date must not be null")
        LocalDate from,

        @NotNull(message = "to date must not be null")
        LocalDate to,

        @NotNull(message = "price must not be null")
        @Positive(message = "price must be greater than zero")
        BigDecimal price

) {}
