package car.rental.app.notification.controller;

import car.rental.app.notification.dto.NotificationRequest;
import car.rental.app.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/reservation-confirmed")
    public ResponseEntity<Void> reservationConfirmed(@RequestBody NotificationRequest request) {
        notificationService.sendReservationConfirmed(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reservation-cancelled")
    public ResponseEntity<Void> reservationCancelled(@RequestBody NotificationRequest request) {
        notificationService.sendReservationCancelled(request);
        return ResponseEntity.ok().build();
    }
}
