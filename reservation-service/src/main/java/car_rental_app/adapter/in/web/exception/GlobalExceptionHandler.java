package car_rental_app.adapter.in.web.exception;

import car_rental_app.adapter.in.web.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ErrorResponse handleApiException(ApiException ex) {
        return new ErrorResponse(
                ex.errorCode(),
                ex.getMessage(),
                Instant.now(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return new ErrorResponse(
                "VALIDATION_ERROR",
                msg,
                Instant.now(),
                null
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        return new ErrorResponse(
                "VALIDATION_ERROR",
                ex.getMessage(),
                Instant.now(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleOther(Exception ex) {
        return new ErrorResponse(
                "INTERNAL_ERROR",
                ex.getMessage(),
                Instant.now(),
                null
        );
    }
}
