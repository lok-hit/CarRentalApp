package car.rental.app.userprofile.controller;

import car.rental.app.userprofile.domain.UserProfile;
import car.rental.app.userprofile.dto.CreateUserProfileRequest;
import car.rental.app.userprofile.dto.UpdatePreferencesRequest;
import car.rental.app.userprofile.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserProfile> create(@RequestBody CreateUserProfileRequest request) {
        return ResponseEntity.ok(service.createProfile(request));
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> list() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-keycloak/{keycloakId}")
    public ResponseEntity<UserProfile> getByKeycloakId(@PathVariable String keycloakId) {
        try {
            return ResponseEntity.ok(service.getByKeycloakId(keycloakId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/preferences")
    public ResponseEntity<UserProfile> updatePreferences(
            @PathVariable String id,
            @RequestBody UpdatePreferencesRequest request) {
        try {
            return ResponseEntity.ok(service.updatePreferences(id, request));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/rental-history/{reservationId}")
    public ResponseEntity<UserProfile> addToRentalHistory(
            @PathVariable String id,
            @PathVariable String reservationId) {
        try {
            return ResponseEntity.ok(service.addToRentalHistory(id, reservationId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
