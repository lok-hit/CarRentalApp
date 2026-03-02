Feature: Car reservation flow

Feature: Car reservation flow

  Scenario: Successfully reserve a car that can be reserved
    Given a car with id "CAR-1" exists and can be reserved by policy
    When a reservation command is handled for car "CAR-1"
    Then the reservation command completes successfully

  Scenario: Reject reservation when policy forbids it
    Given a car with id "CAR-2" exists and cannot be reserved by policy
    When a reservation command is handled for car "CAR-2"
    Then a CarReservationRejectedException is thrown

