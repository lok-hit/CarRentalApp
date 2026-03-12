package car.rental.app.domain.model;

public record CarId(String value) {

    public CarId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CarId cannot be null or blank");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
