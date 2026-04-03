package car.rental.app.adapter.out.persistence.document;

import jakarta.validation.constraints.NotBlank;

public class CarFeatureDocument {
    @NotBlank
    private String feature;

    public CarFeatureDocument() {
    }

    public CarFeatureDocument(String feature) {
        this.feature = feature;
    }

    public String getFeature() {
        return feature;
    }
}