package car_rental_app.application.service;

import car_rental_app.domain.event.ReservationCreatedEvent;
import car_rental_app.domain.model.*;
import car_rental_app.domain.port.EventPublisher;
import car_rental_app.domain.port.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public ReservationId createReservation(
            CarId carId,
            CustomerId customerId,
            DateRange dateRange,
            Money price
    ) {
        policy.validate(dateRange);

        Reservation reservation = new Reservation(carId, customerId, dateRange, price);

        repository.save(reservation);

        reservation.drainEvents().forEach(eventPublisher::publish);

        log.info("Reservation created: {}", reservation.id().value());

        return reservation.id();
    }

    public void confirmReservation(String reservationId) {
        Reservation reservation = repository.findById(new ReservationId(reservationId));

        reservation.confirm();

        repository.save(reservation);

        reservation.drainEvents().forEach(eventPublisher::publish);

        log.info("Reservation confirmed: {}", reservationId);
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = repository.findById(new ReservationId(reservationId));

        reservation.cancel("Cancelled due to failed payment");

        repository.save(reservation);

        reservation.drainEvents().forEach(eventPublisher::publish);

        log.info("Reservation cancelled: {}", reservationId);
    }
}
