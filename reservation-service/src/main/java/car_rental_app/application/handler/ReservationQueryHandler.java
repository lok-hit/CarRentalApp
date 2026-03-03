package car_rental_app.application.handler;

import car_rental_app.application.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationQueryHandler {

    private static final Logger log = LoggerFactory.getLogger(ReservationQueryHandler.class);

    private final ReservationQueryService queryService;

    public ReservationQueryHandler(ReservationQueryService queryService) {
        this.queryService = queryService;
    }

    public ReservationView handle(GetReservationQuery query) {
        log.info("QueryHandler: get reservation {}", query.reservationId());
        return queryService.getReservationDetails(query.reservationId());
    }

    public List<ReservationListItem> handle(ListReservationsByCustomerQuery query) {
        log.info("QueryHandler: list reservations for customer {}", query.customerId());
        return queryService.listByCustomer(query);
    }
}
