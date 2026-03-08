Feature: Create reservation

  Scenario: User creates a reservation successfully
    Given user is authenticated with role USER
    And car with id "car-123" is available
    When user sends POST /reservations with:
      | carId     | car-123                     |
      | startDate | 2026-03-10T10:00:00Z        |
      | endDate   | 2026-03-12T10:00:00Z        |
      | notes     | Child seat                  |
    Then response status should be 201
    And response should contain field "status" with value "CONFIRMED"
    And reservation event "RESERVATION_CREATED" should be published to Kafka
