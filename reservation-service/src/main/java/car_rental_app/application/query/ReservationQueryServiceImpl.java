package car_rental_app.application.query;

import car_rental_app.adapter.in.web.dto.ReservationDetailsResponse;
import car_rental_app.domain.model.Reservation;
import car_rental_app.domain.port.ReservationReadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationReadRepository repository;

    public ReservationQueryServiceImpl(ReservationReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReservationDetailsResponse getReservationDetails(String reservationId) {
        Reservation reservation = repository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));

        return map(reservation);
    }

    @Override
    public List<ReservationDetailsResponse> listReservationsForCustomer(String customerId, int page, int cappedSize) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    private ReservationDetailsResponse map(Reservation r) {
        return new ReservationDetailsResponse(
                r.id().value(),
                r.carId().value(),
                r.customerId().value(),
                r.dateRange().from(),
                r.dateRange().to(),
                r.price().amount(),
                r.price().currency().getCurrencyCode(),
                r.status().name()
        );
    }
}
