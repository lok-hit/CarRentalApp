package car_rental_app.application.service;

import car_rental_app.application.exception.*;
import car_rental_app.domain.event.DomainEvent;
import car_rental_app.domain.model.*;
import car_rental_app.domain.port.EventPublisher;
import car_rental_app.domain.port.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationApplicationService.class);

    private final ReservationRepository repository;
    private final EventPublisher eventPublisher;
    private final ReservationPolicy policy;

    public ReservationApplicationService(
            ReservationRepository repository,
            EventPublisher eventPublisher,
            ReservationPolicy policy
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.policy = policy;
    }

    @Transactional
    public ReservationId createReservation(
            CarId carId,
            CustomerId customerId,
            DateRange dateRange,
            Money price
    ) {
        log.info("Creating reservation for carId={} customerId={}", carId.value(), customerId.value());

        policy.validate(dateRange);

        Reservation reservation = new Reservation(carId, customerId, dateRange, price);

        repository.save(reservation);

        publishDomainEvents(reservation);

        return reservation.id();
    }

    @Transactional
    public void confirmReservation(String reservationId) {
        log.info("Confirming reservation {}", reservationId);

        Reservation reservation = repository.findById(new ReservationId(reservationId));
        if (reservation == null) {
            throw new ReservationNotFoundException(reservationId);
        }

        if (reservation.isConfirmed()) {
            throw new ReservationAlreadyConfirmedException(reservationId);
        }

        if (reservation.isCancelled()) {
            throw new ReservationAlreadyCancelledException(reservationId);
        }

        reservation.confirm();

        repository.save(reservation);

        publishDomainEvents(reservation);
    }

    @Transactional
    public void cancelReservation(String reservationId, String reason) {
        log.info("Cancelling reservation {} with reason={}", reservationId, reason);

        Reservation reservation = repository.findById(new ReservationId(reservationId));
        if (reservation == null) {
            throw new ReservationNotFoundException(reservationId);
        }

        if (reservation.isCancelled()) {
            throw new ReservationAlreadyCancelledException(reservationId);
        }

        reservation.cancel(reason);

        repository.save(reservation);

        publishDomainEvents(reservation);
    }

    private void publishDomainEvents(Reservation reservation) {
        for (DomainEvent event : reservation.drainDomainEvents()) {
            log.info("Publishing domain event {}", event.getClass().getSimpleName());
            eventPublisher.publish(event);
        }
    }
}
