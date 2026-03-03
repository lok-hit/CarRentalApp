package car_rental_app.adapter.in.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateReservationRequest(

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "carId contains invalid characters")
        String carId,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "customerId contains invalid characters")
        String customerId,

        @NotNull
        @FutureOrPresent
        LocalDate startDate,

        @NotNull
        @Future
        LocalDate endDate,

        @NotNull
        @Positive
        BigDecimal priceAmount,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$", message = "priceCurrency must be ISO 4217 (e.g. USD, EUR)")
        String priceCurrency
) {}
