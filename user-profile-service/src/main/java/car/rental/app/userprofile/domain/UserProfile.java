package car.rental.app.userprofile.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "user_profiles")
public class UserProfile {

    @Id
    private String id;
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserPreferences preferences;
    private List<String> rentalHistory;
    private Instant createdAt;
    private Instant updatedAt;

    public UserProfile() {}

    public UserProfile(String id, String keycloakId, String email, String firstName,
                       String lastName, String phone, UserPreferences preferences,
                       List<String> rentalHistory, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.keycloakId = keycloakId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.preferences = preferences;
        this.rentalHistory = rentalHistory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public String getKeycloakId() { return keycloakId; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public UserPreferences getPreferences() { return preferences; }
    public List<String> getRentalHistory() { return rentalHistory; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(String id) { this.id = id; }
    public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPreferences(UserPreferences preferences) { this.preferences = preferences; }
    public void setRentalHistory(List<String> rentalHistory) { this.rentalHistory = rentalHistory; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
