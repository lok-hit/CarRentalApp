package car.rental.app.application.command;

import car.rental.app.domain.model.CarId;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ChangeCarPriceCommand(
        @NotNull CarId id, @NotNull @DecimalMin(value = "0.00") BigDecimal newPrice) {
}
