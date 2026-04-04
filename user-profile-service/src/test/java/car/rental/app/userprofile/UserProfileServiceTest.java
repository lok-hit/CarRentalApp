package car.rental.app.userprofile;

import car.rental.app.userprofile.domain.UserProfile;
import car.rental.app.userprofile.dto.CreateUserProfileRequest;
import car.rental.app.userprofile.dto.UpdatePreferencesRequest;
import car.rental.app.userprofile.repository.UserProfileRepository;
import car.rental.app.userprofile.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository repository;

    private UserProfileService service;

    @BeforeEach
    void setUp() {
        service = new UserProfileService(repository);
    }

    @Test
    void createProfile_savesAndReturnsProfile() {
        CreateUserProfileRequest request = new CreateUserProfileRequest(
                "kc-123", "user@example.com", "Jan", "Kowalski", "+48600000000");

        UserProfile saved = new UserProfile("id-1", "kc-123", "user@example.com",
                "Jan", "Kowalski", "+48600000000", null, new ArrayList<>(),
                Instant.now(), Instant.now());

        when(repository.save(any())).thenReturn(saved);

        UserProfile result = service.createProfile(request);

        assertThat(result.getKeycloakId()).isEqualTo("kc-123");
        assertThat(result.getEmail()).isEqualTo("user@example.com");
        verify(repository).save(any());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById("missing"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void updatePreferences_updatesAndSaves() {
        UserProfile existing = new UserProfile("id-1", "kc-123", "user@example.com",
                "Jan", "Kowalski", null, null, new ArrayList<>(),
                Instant.now(), Instant.now());

        when(repository.findById("id-1")).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UpdatePreferencesRequest request = new UpdatePreferencesRequest("EUR", "en", true);
        UserProfile result = service.updatePreferences("id-1", request);

        assertThat(result.getPreferences().getPreferredCurrency()).isEqualTo("EUR");
        assertThat(result.getPreferences().getPreferredLanguage()).isEqualTo("en");
        assertThat(result.getPreferences().isMarketingConsent()).isTrue();
    }

    @Test
    void addToRentalHistory_appendsReservationId() {
        UserProfile existing = new UserProfile("id-1", "kc-123", "user@example.com",
                "Jan", "Kowalski", null, null, new ArrayList<>(List.of("res-0")),
                Instant.now(), Instant.now());

        when(repository.findById("id-1")).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile result = service.addToRentalHistory("id-1", "res-1");

        assertThat(result.getRentalHistory()).containsExactly("res-0", "res-1");
    }
}
