package car_rental_app.application.query;

import java.math.BigDecimal;

public record CarDto(String id, String category, BigDecimal price, String status) {
}
