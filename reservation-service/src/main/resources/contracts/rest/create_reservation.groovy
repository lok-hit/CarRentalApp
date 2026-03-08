package contracts.rest

Contract.make {
    description "Create reservation"

    request {
        method "POST"
        url "/reservations"
        headers {
            contentType(applicationJson())
        }
        body([
                carId: "car-123",
                startDate: "2026-03-10T10:00:00Z",
                endDate: "2026-03-12T10:00:00Z",
                notes: "Child seat"
        ])
    }

    response {
        status 201
        headers {
            contentType(applicationJson())
        }
        body([
                id: $(regex("[a-zA-Z0-9-]+")),
                customerId: $(regex("[a-zA-Z0-9-]+")),
                carId: "car-123",
                status: "CONFIRMED",
                startDate: "2026-03-10T10:00:00Z",
                endDate: "2026-03-12T10:00:00Z"
        ])
    }
}
