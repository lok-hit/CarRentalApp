package car.rental.app.application.port;

import car.rental.app.application.command.ChangeCarPriceCommand;
import car.rental.app.application.command.CreateCarCommand;
import car.rental.app.application.command.MarkCarAsAvailableCommand;
import car.rental.app.application.command.MarkCarAsUnavailableCommand;

public interface CarCommandPort {
    /**
 * Processes a request to create a new car using the details provided in the command.
 *
 * @param command the command containing data required to create the car (e.g., identifiers, model, attributes, and initial price)
 */
void handle(CreateCarCommand command);

    /**
 * Handles a request to change the rental price of an existing car.
 *
 * @param command the command containing the target car identifier and the new price to apply
 */
void handle(ChangeCarPriceCommand command);

    /**
 * Processes a command to mark a car as unavailable in the system.
 *
 * @param command the command carrying details required to mark the car unavailable
 */
void handle(MarkCarAsUnavailableCommand command);

    /**
 * Marks the car specified by the command as available for rental.
 *
 * @param command the command containing the car identifier and any metadata required to mark it available
 */
void handle(MarkCarAsAvailableCommand command);
}
