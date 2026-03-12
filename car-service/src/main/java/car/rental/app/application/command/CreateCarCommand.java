package car.rental.app.application.command;

import car.rental.app.domain.model.CarCategory;
import car.rental.app.domain.model.CarId;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateCarCommand(
        @NotNull CarId id,
        CarCategory category,
        @NotNull @DecimalMin(value = "0.00") BigDecimal price ) {}
