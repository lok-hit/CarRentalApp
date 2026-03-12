package car.rental.app.domain.port;

import car.rental.app.application.command.ChangeCarPriceCommand;
import car.rental.app.application.command.CreateCarCommand;
import car.rental.app.application.command.MarkCarAsAvailableCommand;
import car.rental.app.application.command.MarkCarAsUnavailableCommand;

public interface CarCommandPort {
    void handle(CreateCarCommand command);

    void handle(ChangeCarPriceCommand command);

    void handle(MarkCarAsUnavailableCommand command);

    void handle(MarkCarAsAvailableCommand command);
}
