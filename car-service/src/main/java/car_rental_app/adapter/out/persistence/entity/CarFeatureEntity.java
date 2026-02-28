package car_rental_app.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "car_features")
public class CarFeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String feature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private CarEntity car;

    protected CarFeatureEntity() {
    }

    public CarFeatureEntity(String feature) {
        this.feature = feature;
    }

    public Long getId() {
        return id;
    }

    public String getFeature() {
        return feature;
    }

    public CarEntity getCar() {
        return car;
    }

    public void setCar(CarEntity car) {
        this.car = car;
    }
}
