package car.rental.app.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationDetailsResponse(
        String reservationId,
        String carId,
        String customerId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal priceAmount,
        String priceCurrency,
        String status
) {}
