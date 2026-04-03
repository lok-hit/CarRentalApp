package car.rental.app.application.query.mapper;


import car.rental.app.application.query.CarDto;
import car.rental.app.domain.model.Car;

public class CarMapper {


    private CarMapper (){}
    /**
     * Map a car_rental_app.domain Car to a CarDto containing its identifier, category, price, and status.
     *
     * @param car the car_rental_app.domain Car to convert
     * @return a CarDto populated with the car's identifier, category name, price value, and status name
     */
    public static CarDto toDto(Car car) {
        return new CarDto(
                car.id().value(),
                car.category().name(),
                car.price().value(),
                car.status().name());
    }
}
