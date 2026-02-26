package car_rental_app.domain.port;

public interface ReservationEventsPort {
    void onReservationCreated(ReservationCreatedEvent event);

    void onReservationCancelled(ReservationCancelledEvent event);

    void onPaymentFailed(PaymentFailedEvent event);
}
