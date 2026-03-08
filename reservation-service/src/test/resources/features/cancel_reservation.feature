Feature: Cancel reservation

  Scenario: User cancels an existing reservation
    Given user is authenticated with role USER
    And reservation with id "res-123" exists
    When user sends DELETE /reservations/res-123
    Then response status should be 204
    And reservation event "RESERVATION_CANCELLED" should be published to Kafka
