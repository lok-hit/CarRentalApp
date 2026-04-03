package car.rental.app.userprofile.dto;

public record CreateUserProfileRequest(
        String keycloakId,
        String email,
        String firstName,
        String lastName,
        String phone
) {}
