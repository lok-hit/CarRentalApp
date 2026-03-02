package car_rental_app.domain.service;

import car_rental_app.domain.model.AvailabilityStatus;
import car_rental_app.domain.model.Car;
import car_rental_app.domain.port.ReservationPolicy;
import org.springframework.stereotype.Component;

@Component
public class DefaultReservationPolicy implements ReservationPolicy {
    
    @Override
    public boolean canBeReserved(Car car) {
        return car.status() == AvailabilityStatus.AVAILABLE;
    }
}
