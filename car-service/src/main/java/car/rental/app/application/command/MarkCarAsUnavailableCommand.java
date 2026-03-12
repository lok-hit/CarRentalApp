package car.rental.app.application.command;

import jakarta.validation.constraints.NotNull;

public record MarkCarAsUnavailableCommand(@NotNull String id) {
}
