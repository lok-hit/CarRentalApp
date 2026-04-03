package car.rental.app.userprofile.dto;

public record UpdatePreferencesRequest(
        String preferredCurrency,
        String preferredLanguage,
        boolean marketingConsent
) {}
