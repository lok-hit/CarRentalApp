package car_rental_app.domain.port;

public interface CarCommandPort {
    void handle(CreateCarCommand command);

    void handle(ChangeCarPriceCommand command);

    void handle(MarkCarAsUnavailableCommand command);

    void handle(MarkCarAsAvailableCommand command);
}
