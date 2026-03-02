Feature: Changing car price

  Scenario: Change price enforces path id
    Given a ChangeCarPriceCommand with id "WRONG-ID" and new price "100"
    When PUT "/cars/CAR-4/price" is called with this body
    Then the request is rejected with BadRequest
    And the application service is not invoked
