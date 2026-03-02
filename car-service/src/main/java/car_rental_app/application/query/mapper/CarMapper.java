package car_rental_app.application.query.mapper;


import car_rental_app.application.query.CarDto;
import car_rental_app.domain.model.Car;

import java.util.stream.Collectors;

public class CarMapper {

    public static CarDto toDto(Car car) {
        return new CarDto(
                car.id().value(),
                car.category().name(),
                car.price().value(),
                car.status().name());
    }
}
