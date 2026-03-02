package car_rental_app.application.handler;

import car_rental_app.application.command.CancelReservationCommand;
import car_rental_app.application.command.ConfirmReservationCommand;
import car_rental_app.application.command.CreateReservationCommand;
import car_rental_app.application.service.ReservationApplicationService;
import car_rental_app.domain.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReservationCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(ReservationCommandHandler.class);

    private final ReservationApplicationService service;

    public ReservationCommandHandler(ReservationApplicationService service) {
        this.service = service;
    }

    public ReservationId handle(CreateReservationCommand cmd) {
        log.info("CommandHandler: create reservation carId={} customerId={}", cmd.carId(), cmd.customerId());

        return service.createReservation(
                new CarId(cmd.carId()),
                new CustomerId(cmd.customerId()),
                new DateRange(cmd.from(), cmd.to()),
                new Money(cmd.price())
        );
    }

    public void handle(ConfirmReservationCommand cmd) {
        log.info("CommandHandler: confirm reservation {}", cmd.reservationId());
        service.confirmReservation(cmd.reservationId());
    }

    public void handle(CancelReservationCommand cmd) {
        log.info("CommandHandler: cancel reservation {}", cmd.reservationId());
        service.cancelReservation(cmd.reservationId());
    }
}
