package contracts.kafka

Contract.make {
    description "Reservation created Kafka event"

    label "reservation_created"

    input {
        triggeredBy("triggerReservationCreated()")
    }

    outputMessage {
        sentTo("reservation.events")
        body([
                eventType: "RESERVATION_CREATED",
                reservationId: $(regex("[a-zA-Z0-9-]+")),
                customerId: $(regex("[a-zA-Z0-9-]+")),
                carId: "car-123"
        ])
        headers {
            messagingContentType(applicationJson())
        }
    }
}
