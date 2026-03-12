package car.rental.app.payment.application.validation;

@FunctionalInterface
public interface ValidationRule <T>{
    void validate(T target);
}
