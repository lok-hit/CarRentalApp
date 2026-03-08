package contracts.rest

Contract.make {
    description "List reservations for current user"

    request {
        method GET()
        url "/reservations"
        headers {
            accept(applicationJson())
        }
    }

    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
                content: [
                        [
                                id: $(regex("[a-zA-Z0-9-]+")),
                                carId: "car-123",
                                status: "CONFIRMED"
                        ]
                ],
                page: 0,
                size: 20,
                totalElements: 1,
                totalPages: 1
        ])
    }
}
