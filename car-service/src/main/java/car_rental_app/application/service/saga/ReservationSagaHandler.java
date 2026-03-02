package car_rental_app.application.service.saga;

import car_rental_app.application.command.MarkCarAsAvailableCommand;
import car_rental_app.application.command.MarkCarAsUnavailableCommand;
import car_rental_app.application.port.CarCommandPort;
import car_rental_app.domain.port.OutboxEventStore;
import car_rental_app.domain.saga.event.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class ReservationSagaHandler {

    private static final Logger log = Logger.getLogger(ReservationSagaHandler.class.getName());
    private final CarCommandPort carCommandPort;
    private final OutboxEventStore outboxEventStore;

    public ReservationSagaHandler(CarCommandPort carCommandPort, OutboxEventStore outboxEventStore) {
        this.carCommandPort = carCommandPort;
        this.outboxEventStore = outboxEventStore;
    }

    @Transactional
    public void onReservationConfirmed(ReservationConfirmedEvent event) {
        try {
            carCommandPort.handle(new MarkCarAsUnavailableCommand(event.getCarId()));
            log.info("[traceId={}] Car marked unavailable carId={}" + event.getCarId());
            outboxEventStore.saveEvent(trace(), event);
            log.info("[traceId={}] Outbox stored ReservationConfirmedEvent reservationId={}" + event.getReservationId());
            log.info("[traceId={}] Saga END: onReservationConfirmed OK reservationId={}" + event.getReservationId());
        } catch (Exception ex) {
            log.severe("[traceId={}] Saga ERROR onReservationConfirmed reservationId={}, error={}" + event.getReservationId() + ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void onReservationCreated(ReservationCreatedEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationCreated car=" + event.carId());
        carCommandPort.handle(new MarkCarAsUnavailableCommand(event.carId().value()));

        outboxEventStore.saveEvent(event.reservationId(), event);
    }

    @Transactional
    public void onReservationCancelled(ReservationCancelledEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationCancelled car=" + event.carId());
        carCommandPort.handle(new MarkCarAsAvailableCommand(event.carId()));

        outboxEventStore.saveEvent(event.reservationId(), event);
    }

    @Transactional
    public void onReservationFailed(PaymentFailedEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationFailed car=" + event.carId());
        carCommandPort.handle(new MarkCarAsAvailableCommand(event.carId()));

        outboxEventStore.saveEvent(event.reservationId(), event);
    }

    @Transactional
    public void onPaymentConfirmed(PaymentConfirmedEvent event) {
        String traceId = MDC.get("traceId");
        log.info("[traceId={}] Saga START: onPaymentCompleted reservationId={}, carId={}, userId={}");

        try {
            carCommandPort.handle(new MarkCarAsUnavailableCommand(event.getCarId()));
            log.info("[traceId={}] Car marked unavailable carId={}");
            log.info("[traceId={}] Outbox stored PaymentCompletedEvent reservationId={}");
            log.info("[traceId={}] Saga END: onPaymentCompleted OK reservationId={}");
        } catch (Exception ex) {
            log.severe("[traceId={}] Saga ERROR onPaymentCompleted reservationId={}, error={}" + ex);
            throw ex;
        }
    }

    private String trace() {
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        return "trace=" + (traceId != null ? traceId : "none") + " span=" + (spanId != null ? spanId : "none");
    }
}
