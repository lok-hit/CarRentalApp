package car.rental.app.adapter.out.persistence;

import car.rental.app.adapter.out.persistence.entity.ReservationEntity;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.model.CustomerId;
import car.rental.app.domain.model.DateRange;
import car_rental_app.domain.model.*;
import car.rental.app.domain.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

@Component
public class ReservationMapper {

    public ReservationEntity toEntity(Reservation reservation) {
        return ReservationEntity.builder()
                .customerId(reservation.customerId().toString())
                .carId(reservation.carId().toString())
                .status(reservation.status())
                .startDate(Instant.from(reservation.dateRange().from()))
                .endDate(Instant.from(reservation.dateRange().to()))
                .notes(reservation.toString())
                .build();
    }

    public Reservation toDomain(ReservationEntity entity) {
        return new Reservation( new CarId(entity.getCarId()), new CustomerId(entity.getId()),
                new DateRange(LocalDate.from(entity.getStartDate()),
                        LocalDate.from(entity.getEndDate())), entity.getPrice());
    }
}
