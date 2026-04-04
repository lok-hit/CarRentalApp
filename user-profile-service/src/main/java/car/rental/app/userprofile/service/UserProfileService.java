package car.rental.app.userprofile.service;

import car.rental.app.userprofile.domain.UserPreferences;
import car.rental.app.userprofile.domain.UserProfile;
import car.rental.app.userprofile.dto.CreateUserProfileRequest;
import car.rental.app.userprofile.dto.UpdatePreferencesRequest;
import car.rental.app.userprofile.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public UserProfile createProfile(CreateUserProfileRequest request) {
        UserProfile profile = new UserProfile(
                null,
                request.keycloakId(),
                request.email(),
                request.firstName(),
                request.lastName(),
                request.phone(),
                new UserPreferences("PLN", "pl", false),
                new ArrayList<>(),
                Instant.now(),
                Instant.now()
        );
        return repository.save(profile);
    }

    public UserProfile getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User profile not found: " + id));
    }

    public UserProfile getByKeycloakId(String keycloakId) {
        return repository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NoSuchElementException("User profile not found for keycloakId: " + keycloakId));
    }

    public UserProfile updatePreferences(String id, UpdatePreferencesRequest request) {
        UserProfile profile = getById(id);
        profile.setPreferences(new UserPreferences(
                request.preferredCurrency(),
                request.preferredLanguage(),
                request.marketingConsent()
        ));
        profile.setUpdatedAt(Instant.now());
        return repository.save(profile);
    }

    public UserProfile addToRentalHistory(String id, String reservationId) {
        UserProfile profile = getById(id);
        List<String> history = new ArrayList<>(profile.getRentalHistory());
        history.add(reservationId);
        profile.setRentalHistory(history);
        profile.setUpdatedAt(Instant.now());
        return repository.save(profile);
    }

    public List<UserProfile> listAll() {
        return repository.findAll();
    }

    public void deleteProfile(String id) {
        repository.deleteById(id);
    }
}
