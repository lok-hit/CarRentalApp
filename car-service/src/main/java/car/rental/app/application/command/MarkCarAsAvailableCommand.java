package car.rental.app.application.command;

import car.rental.app.domain.model.CarId;
import jakarta.validation.constraints.NotNull;

public record MarkCarAsAvailableCommand(@NotNull CarId id) {
}
