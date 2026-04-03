package car.rental.app.notification;

import car.rental.app.notification.controller.NotificationController;
import car.rental.app.notification.dto.NotificationRequest;
import car.rental.app.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void reservationConfirmed_returns200() throws Exception {
        NotificationRequest request = new NotificationRequest(
                "res-1", "cust-1", "user@example.com", Instant.now(), null);

        mockMvc.perform(post("/notifications/reservation-confirmed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(notificationService).sendReservationConfirmed(any());
    }

    @Test
    void reservationCancelled_returns200() throws Exception {
        NotificationRequest request = new NotificationRequest(
                "res-2", "cust-2", "user@example.com", Instant.now(), "customer request");

        mockMvc.perform(post("/notifications/reservation-cancelled")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(notificationService).sendReservationCancelled(any());
    }
}
