package car_rental_app.application.query;

import car_rental_app.adapter.in.web.dto.ReservationDetailsResponse;

import java.util.List;

public interface ReservationQueryService {

    ReservationDetailsResponse getReservationDetails(String reservationId);

    List<ReservationDetailsResponse> listReservationsForCustomer(String customerId, int page, int cappedSize);
}
