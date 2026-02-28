package car_rental_app.application.service.saga;

import car_rental_app.application.command.MarkCarAsAvailableCommand;
import car_rental_app.application.command.MarkCarAsUnavailableCommand;
import car_rental_app.domain.port.CarCommandPort;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;


@Service
public class ReservationSagaHandler {

    private static final Logger log = Logger.getLogger(ReservationSagaHandler.class.getName());
    private final CarCommandPort carCommandPort;

    @Autowired
    public ReservationSagaHandler(CarCommandPort carCommandPort) {
        this.carCommandPort = carCommandPort;
    }

    @Transactional
    public void onReservationCreated(ReservationCreatedEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationCreated car=" + event.carId());
        carCommandPort.handle(new MarkCarAsUnavailableCommand(event.carId()));
    }

    @Transactional
    public void onReservationCancelled(ReservationCancelledEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationCancelled car=" + event.carId());
        carCommandPort.handle(new MarkCarAsAvailableCommand(event.carId()));
    }

    @Transactional
    public void onReservationFailed(PaymentFailedEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationFailed car=" + event.carId());
        carCommandPort.handle(new MarkCarAsAvailableCommand(event.carId()));
    }

    private String trace() {
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        return "trace=" + (traceId != null ? traceId : "none") + " span=" + (spanId != null ? spanId : "none");
    }
}
