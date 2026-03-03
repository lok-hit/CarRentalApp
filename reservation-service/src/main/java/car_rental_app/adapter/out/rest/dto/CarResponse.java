package car_rental_app.adapter.out.rest;

public record CarResponse(
        String carId,
        String brand,
        String model,
        boolean available
) {}
