package car.rental.app.adapter.out.persistence.mapper;

import car.rental.app.adapter.out.persistence.document.CarCategoryDocument;
import car.rental.app.adapter.out.persistence.document.CarDocument;
import car.rental.app.adapter.out.persistence.document.StatusDocument;
import car.rental.app.domain.model.Car;
import car.rental.app.domain.model.CarCategory;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.model.Price;
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
