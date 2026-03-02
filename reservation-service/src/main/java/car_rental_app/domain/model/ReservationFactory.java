package car_rental_app.domain.model;

public class ReservationFactory {

    private final ReservationPolicy policy;

    public ReservationFactory(ReservationPolicy policy) {
        this.policy = policy;
    }

    public Reservation create(CarId carId, CustomerId customerId, DateRange range, Money price) {
        policy.validate(range);
        return new Reservation(carId, customerId, range, price);
    }
}

