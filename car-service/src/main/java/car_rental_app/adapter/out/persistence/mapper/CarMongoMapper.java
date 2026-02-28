package car_rental_app.adapter.out.persistence.mapper;

import car_rental_app.adapter.out.persistence.document.CarCategoryDocument;
import car_rental_app.adapter.out.persistence.document.CarDocument;
import car_rental_app.adapter.out.persistence.document.StatusDocument;
import car_rental_app.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public final class CarMongoMapper {

    private CarMongoMapper() {

    }

    public static CarDocument toDocument(Car car) {

        return new CarDocument(
                car.id().value(),
                CarCategoryDocument.valueOf(car.category().name()),
                car.price().value(),
                StatusDocument.valueOf(car.status().name())
        );

    }

    public static Car toDomain(CarDocument doc) {
        return new Car(new CarId(doc.getId()),
                CarCategory.valueOf(doc.getCategory().name()),
                new Price(doc.getPrice()));
    }
}
