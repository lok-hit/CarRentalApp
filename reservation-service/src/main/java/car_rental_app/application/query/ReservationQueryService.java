package car_rental_app.application.query;

import java.util.List;

public interface ReservationQueryService {

    ReservationView getReservation(GetReservationQuery query);

    List<ReservationListItem> listByCustomer(ListReservationsByCustomerQuery query);
}
