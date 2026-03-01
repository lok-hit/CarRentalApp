package car_rental_app.application.command;

import car_rental_app.domain.model.CarId;
import jakarta.validation.constraints.NotNull;

public record MarkCarAsAvailableCommand(@NotNull CarId id) {
}
