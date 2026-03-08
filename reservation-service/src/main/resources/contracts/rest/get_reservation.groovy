package contracts.rest

Contract.make {
    description "Get reservation by ID"

    request {
        method "GET"
        urlPath($(regex("/reservations/[a-zA-Z0-9-]+")))
    }

    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
                id: $(regex("[a-zA-Z0-9-]+")),
                customerId: $(regex("[a-zA-Z0-9-]+")),
                carId: "car-123",
                status: "CONFIRMED"
        ])
    }
}
