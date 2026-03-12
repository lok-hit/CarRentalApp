package car.rental.app.application.query;

import car.rental.app.adapter.in.web.dto.ReservationDetailsResponse;

import java.util.List;

public interface ReservationQueryService {

    ReservationDetailsResponse getReservationDetails(String reservationId);

    List<ReservationDetailsResponse> listReservationsForCustomer(String customerId, int page, int cappedSize);
}
