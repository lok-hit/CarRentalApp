package car.rental.app.application.handler;

import car.rental.app.application.command.CancelReservationCommand;
import car.rental.app.application.command.ConfirmReservationCommand;
import car.rental.app.application.command.CreateReservationCommand;
import car.rental.app.application.service.ReservationApplicationService;
import car.rental.app.domain.model.*;
import car_rental_app.domain.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.Currency;

@Service
public class ReservationCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(ReservationCommandHandler.class);

    private final ReservationApplicationService service;

    public ReservationCommandHandler(ReservationApplicationService service) {
        this.service = service;
    }

    public ReservationId handle(@Valid CreateReservationCommand cmd) {
        log.info("CommandHandler: create reservation carId={} customerId={}", cmd.carId(), cmd.customerId());

        return service.createReservation(
                new CarId(cmd.carId()),
                new CustomerId(cmd.customerId()),
                new DateRange(cmd.from(), cmd.to()),
                new Money(cmd.price(), Currency.getInstance(cmd.currency()))
        );
    }

    public void handle(@Valid ConfirmReservationCommand cmd) {
        log.info("CommandHandler: confirm reservation {}", cmd.reservationId());
        service.confirmReservation(cmd.reservationId());
    }

    public void handle(@Valid CancelReservationCommand cmd) {
        log.info("CommandHandler: cancel reservation {}", cmd.reservationId());
        service.cancelReservation(cmd.reservationId(), cmd.reason());
    }
}
