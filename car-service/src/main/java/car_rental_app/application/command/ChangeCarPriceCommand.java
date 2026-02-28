package car_rental_app.application.command;

import car_rental_app.domain.model.CarId;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ChangeCarPriceCommand(
        @NotNull CarId id, @NotNull @DecimalMin(value = "0.00") BigDecimal newPrice) {
}
