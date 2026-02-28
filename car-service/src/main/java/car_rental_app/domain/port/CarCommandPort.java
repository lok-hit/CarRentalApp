package car_rental_app.domain.port;

import car_rental_app.application.command.ChangeCarPriceCommand;
import car_rental_app.application.command.CreateCarCommand;
import car_rental_app.application.command.MarkCarAsAvailableCommand;
import car_rental_app.application.command.MarkCarAsUnavailableCommand;

public interface CarCommandPort {
    void handle(CreateCarCommand command);

    void handle(ChangeCarPriceCommand command);

    void handle(MarkCarAsUnavailableCommand command);

    void handle(MarkCarAsAvailableCommand command);
}
