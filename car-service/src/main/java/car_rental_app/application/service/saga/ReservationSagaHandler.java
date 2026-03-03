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

    /**
     * Create a ReservationSagaHandler using the given car command port and outbox event store.
     */
    public ReservationSagaHandler(CarCommandPort carCommandPort, OutboxEventStore outboxEventStore) {
        this.carCommandPort = carCommandPort;
        this.outboxEventStore = outboxEventStore;
    }

    /**
     * Handles a ReservationConfirmedEvent by marking the associated car as unavailable and logging saga completion.
     *
     * @param event the confirmed reservation event containing the reservation and car identifiers
     */
    @Transactional
    public void onReservationConfirmed(ReservationConfirmedEvent event) {
        try {
            carCommandPort.handle(new MarkCarAsUnavailableCommand(event.getCarId()));
            log.info("[traceId={}] Car marked unavailable carId={}" + event.getCarId());
            log.info("[traceId={}] Saga END: onReservationConfirmed OK reservationId={}" + event.getReservationId());
        } catch (Exception ex) {
            log.severe("[traceId={}] Saga ERROR onReservationConfirmed reservationId={}, error={}" + event.getReservationId() + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Handles a reservation creation by marking the associated car as unavailable.
     *
     * @param event the ReservationCreatedEvent containing the reservation and car identifiers
     */
    @Transactional
    public void onReservationCreated(ReservationCreatedEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationCreated car=" + event.carId());
        carCommandPort.handle(new MarkCarAsUnavailableCommand(event.carId().value()));
    }

    /**
     * Handles a reservation cancellation by marking the associated car as available.
     *
     * Sends a MarkCarAsAvailableCommand for the car referenced in the provided event.
     *
     * @param event the reservation cancellation event containing the carId to be marked available
     */
    @Transactional
    public void onReservationCancelled(ReservationCancelledEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationCancelled car=" + event.carId());
        carCommandPort.handle(new MarkCarAsAvailableCommand(event.carId()));
    }

    /**
     * Handles a reservation failure by marking the associated car as available.
     *
     * Sends a MarkCarAsAvailableCommand for the car referenced by the event.
     *
     * @param event the payment-failed event containing the reservation and car identifier
     */
    @Transactional
    public void onReservationFailed(PaymentFailedEvent event) {
        log.info(() -> "[" + trace() + "] Saga: ReservationFailed car=" + event.carId());
        carCommandPort.handle(new MarkCarAsAvailableCommand(event.carId()));
    }

    /**
     * Handles a confirmed payment by marking the associated car unavailable and persisting a ReservationConfirmedEvent to the outbox.
     *
     * @param event the PaymentConfirmedEvent containing the reservationId, carId, and userId used to mark the car and create the outbox event
     */
    @Transactional
    public void onPaymentConfirmed(PaymentConfirmedEvent event) {
        String traceId = MDC.get("traceId");
        log.info("[traceId={}] Saga START: onPaymentCompleted reservationId={}, carId={}, userId={}");

        try {
            carCommandPort.handle(new MarkCarAsUnavailableCommand(event.getCarId()));
            log.info("[traceId={}] Car marked unavailable carId={}");
            
            // Create and save ReservationConfirmedEvent to outbox
            ReservationConfirmedEvent confirmedEvent = new ReservationConfirmedEvent(
                event.getReservationId(),
                event.getCarId(),
                event.getUserId()
            );
            outboxEventStore.saveEvent(event.getReservationId(), confirmedEvent);
            
            log.info("[traceId={}] ReservationConfirmedEvent saved to outbox reservationId={}" + event.getReservationId());
            log.info("[traceId={}] Saga END: onPaymentCompleted OK reservationId={}" + event.getReservationId());
        } catch (Exception ex) {
            log.severe("[traceId={}] Saga ERROR onPaymentCompleted reservationId={}, error={}" + event.getReservationId() + ex);
            throw ex;
        }
    }

    /**
     * Builds a formatted trace string from the MDC trace and span identifiers.
     *
     * @return the formatted string "trace=<traceId> span=<spanId>" where missing values are replaced with "none"
     */
    private String trace() {
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        return "trace=" + (traceId != null ? traceId : "none") + " span=" + (spanId != null ? spanId : "none");
    }
}
