package contracts.kafka

Contract.make {
    description "Reservation cancelled Kafka event"

    label "reservation_cancelled"

    input {
        triggeredBy("triggerReservationCancelled()")
    }

    outputMessage {
        sentTo("reservation.events")
        body([
                eventType: "RESERVATION_CANCELLED",
                reservationId: $(regex("[a-zA-Z0-9-]+")),
                reason: "USER_CANCELLED"
        ])
        headers {
            messagingContentType(applicationJson())
        }
    }
}
