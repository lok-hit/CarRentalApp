package car_rental_app.application.service.saga;

import car_rental_app.application.service.ReservationApplicationService;
import car_rental_app.domain.event.*;
import car_rental_app.domain.model.Reservation;
import car_rental_app.domain.port.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReservationSagaHandler {

    private static final Logger log = LoggerFactory.getLogger(ReservationSagaHandler.class);

    private final ReservationRepository reservationRepository;
    private final ReservationApplicationService reservationService;

    public ReservationSagaHandler(ReservationRepository reservationRepository, ReservationApplicationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }

    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Saga: Payment completed for reservationId={}", event.reservationId());
        reservationService.confirmReservation(event.reservationId());
    }

    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Saga: Payment failed for reservationId={} reason={}", event.reservationId(), event.reason());
        reservationService.cancelReservation(event.reservationId(), event.reason());
    }

    public void handleCarCreated(CarCreatedEvent event) {

        log.info("Car created in fleet: {}", event.carId());
        // opcjonalnie: aktualizacja read modelu
    }

    public void handleCarAvailable(CarMarkedAsAvailableEvent event) {
        log.info("Car {} marked as available", event.carId());
        reservationService.markCarAsAvailable(event.carId());
    }

    public void handleCarUnavailable(CarMarkedAsUnavailableEvent event) {
        log.warn("Car {} marked as unavailable. Reason: {}", event.carId(), event.reason());
        List<Reservation> active = reservationRepo.findActiveByCarId(event.carId());
        for (Reservation r : active) {
            log.warn("Cancelling reservation {} because car {} is unavailable", r.id().value(), event.carId());
            reservationService.cancelDueToCarUnavailable(r.id().value(), event.reason());
        }
    }
}

