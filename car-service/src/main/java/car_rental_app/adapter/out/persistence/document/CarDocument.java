package car_rental_app.adapter.out.persistence.document;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "cars")
public class CarDocument {

    @Id
    @NotBlank
    private String id;

    @NotNull
    private CarCategoryDocument category;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    private StatusDocument status;

    @Valid
    @Size(max = 50)
    private List<CarFeatureDocument> features;

    @Valid
    @Size(max = 50)
    private List<CarImageDocument> images;

    @Valid
    @Size(max = 200)
    private List<CarMaintenanceRecordDocument> maintenanceRecords;

    public CarDocument() {
    }

    public CarDocument(String id, CarCategoryDocument category, BigDecimal price, StatusDocument status){

        this.id = id;
        this.category = category;
        this.price = price;
        this.status = status;
    }
    public CarDocument(String id,
                       CarCategoryDocument category,
                       BigDecimal price,
                       StatusDocument status,
                       List<CarFeatureDocument> features,
                       List<CarImageDocument> images,
                       List<CarMaintenanceRecordDocument> maintenanceRecords) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.status = status;
        this.features = features;
        this.images = images;
        this.maintenanceRecords = maintenanceRecords;
    }

    public String getId() {
        return id;
    }

    public CarCategoryDocument getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public StatusDocument getStatus() {
        return status;
    }

    public List<CarFeatureDocument> getFeatures() {
        return features;
    }

    public List<CarImageDocument> getImages() {
        return images;
    }

    public List<CarMaintenanceRecordDocument> getMaintenanceRecords() {
        return maintenanceRecords;
    }
}
