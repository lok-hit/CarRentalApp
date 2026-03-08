package car_rental_app.adapter.out.persistence.entity;

import car_rental_app.domain.model.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "reservations")
@Data
@Builder
public class ReservationEntity {

    @Id
    private String id;

    @Indexed
    private String customerId;

    @Indexed
    private String carId;

    private ReservationStatus status;

    private Instant startDate;
    private Instant endDate;

    private String notes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public ReservationEntity() {}

    public ReservationEntity(
            String id,
            String customerId,
            String carId,
            ReservationStatus status,
            Instant startDate,
            Instant endDate,
            String notes,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.customerId = customerId;
        this.carId = carId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}
