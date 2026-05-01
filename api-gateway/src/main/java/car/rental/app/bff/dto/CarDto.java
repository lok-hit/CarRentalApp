package car.rental.app.bff.dto;

public record CarDto(
        String id,
        String category,
        double price,
        String status
) {
    public boolean available() {
        return "AVAILABLE".equals(status);
    }
}
