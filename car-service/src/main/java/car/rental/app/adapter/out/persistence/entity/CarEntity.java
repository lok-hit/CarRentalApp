package car.rental.app.adapter.out.persistence.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Pattern(regexp = "^[A-Za-z]+$")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_category", nullable = false)
    private CarCategoryEntity carCategory;

    @Column(name = "rent_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal rentPrice;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AvailabilityStatusEntity statusEntity;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private HashSet<CarFeatureEntity> features = new HashSet<>();

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private HashSet<CarImageEntity> images = new HashSet<>();

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private HashSet<CarMaintenanceRecordEntity> maintenanceRecords = new HashSet<>();

    protected CarEntity() { // JPA only
    }

    public CarEntity(String id, CarCategoryEntity carCategory, BigDecimal rentPrice, AvailabilityStatusEntity statusEntity) {
        this.id = id;
        this.carCategory = carCategory;
        this.rentPrice = rentPrice;
        this.statusEntity = statusEntity;
    }

    public String getId() {
        return id;
    }

    public CarCategoryEntity getCategory() {
        return carCategory;
    }

    public BigDecimal getPrice() {
        return rentPrice;
    }

    public AvailabilityStatusEntity getStatus() {
        return statusEntity;
    }

    public Set<CarFeatureEntity> getFeatures() {
        return features;
    }

    public Set<CarImageEntity> getImages() {
        return images;
    }

    public Set<CarMaintenanceRecordEntity> getMaintenanceRecords() {
        return maintenanceRecords;
    }

    public void addFeature(CarFeatureEntity feature) {
        features.add(feature);
        feature.setCar(this);
    }

    public void addImage(CarImageEntity image) {
        images.add(image);
        image.setCar(this);
    }

    public void addMaintenanceRecord(CarMaintenanceRecordEntity entity) {
        maintenanceRecords.add(entity);
        entity.setCar(this);
    }
}
