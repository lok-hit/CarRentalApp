package car_rental_app.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "car_maintenance_records")
public class CarMaintenanceRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private CarEntity car;

    protected CarMaintenanceRecordEntity() {
    }

    public CarMaintenanceRecordEntity(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public CarEntity getCar() {
        return car;
    }

    public void setCar(CarEntity car) {
        this.car = car;
    }
}
