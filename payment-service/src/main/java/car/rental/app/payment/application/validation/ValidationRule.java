package car.rental.app.payment.application.validation;

public interface ValidationRule <T>{
    void validate(T target);
}
