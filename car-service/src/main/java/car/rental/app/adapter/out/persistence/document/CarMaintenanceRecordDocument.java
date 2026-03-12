package car.rental.app.adapter.out.persistence.document;

import jakarta.validation.constraints.NotBlank;

public class CarMaintenanceRecordDocument {

    @NotBlank
    private String description;

    public CarMaintenanceRecordDocument() {}

    public CarMaintenanceRecordDocument(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
