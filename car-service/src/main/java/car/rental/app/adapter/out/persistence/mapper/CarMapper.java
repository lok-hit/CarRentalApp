package car.rental.app.adapter.out.persistence.mapper;

import car.rental.app.adapter.out.persistence.entity.AvailabilityStatusEntity;
import car.rental.app.adapter.out.persistence.entity.CarCategoryEntity;
import car.rental.app.adapter.out.persistence.entity.CarEntity;
import car.rental.app.domain.model.Car;
import car.rental.app.domain.model.CarCategory;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.model.Price;
import car_rental_app.domain.model.*;

public class CarMapper {

    private CarMapper() {

    }

    public static CarEntity toEntity(Car car) {
        return new CarEntity(car.id().value(),
                CarCategoryEntity.valueOf(car.category().name()), car.price().value(),
                AvailabilityStatusEntity.valueOf(car.status().name()));
    }

    public static Car toDomain(CarEntity entity) {
        return new Car(new CarId(entity.getId()), CarCategory.valueOf(entity.getCategory().name()),
                new Price(entity.getPrice()));
    }
}

