package car.rental.app.adapter.out.rest.dto;

public record CarResponse(
        String carId,
        String brand,
        String model,
        boolean available
) {}
