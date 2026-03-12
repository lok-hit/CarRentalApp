package car.rental.app.adapter.in.web.dto;



import java.time.Instant;

public record ErrorResponse(
        String error,
        String message,
        Instant timestamp,
        String path
) {}
