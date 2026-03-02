Feature: Querying cars

  Scenario: Get car by id returns CarDto
    Given a car with id "CAR-5" exists in the system
    When GET "/cars/CAR-5" is called
    Then the response status is 200
    And the response contains a CarDto

  Scenario: Get available cars returns list of CarDto
    Given multiple available cars exist
    When GET "/cars" is called
    Then the response contains a list of CarDto
