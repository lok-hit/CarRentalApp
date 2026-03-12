package car.rental.app.domain.service;

import car.rental.app.domain.model.AvailabilityStatus;
import car.rental.app.domain.model.Car;
import car.rental.app.domain.port.ReservationPolicy;
import org.springframework.stereotype.Component;

@Component
public class DefaultReservationPolicy implements ReservationPolicy {
    
    /**
     * Determine whether a car is currently reservable.
     *
     * @param car the car to check
     * @return `true` if the car's availability status is `AVAILABLE`, `false` otherwise
     */
    @Override
    public boolean canBeReserved(Car car) {
        return car.status() == AvailabilityStatus.AVAILABLE;
    }
}
