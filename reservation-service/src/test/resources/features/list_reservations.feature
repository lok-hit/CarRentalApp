Feature: List reservations

  Scenario: User lists their reservations
    Given user is authenticated with role USER
    And user has 1 reservation
    When user sends GET /reservations
    Then response status should be 200
    And response should contain list with size 1
