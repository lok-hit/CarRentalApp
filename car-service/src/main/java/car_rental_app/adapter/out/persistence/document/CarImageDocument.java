package car_rental_app.adapter.out.persistence.document;

import jakarta.validation.constraints.NotBlank;

public class CarImageDocument {
    @NotBlank
    private String url;

    public CarImageDocument() {
    }

    public CarImageDocument(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
