package car.rental.app.bff.dto;

public record CarDto(
        String id,
        String type,
        String brand,
        String model,
        int year,
        String registrationNumber,
        int mileage,
        boolean available
) {}
