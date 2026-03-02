Feature: Domain event lifecycle

  Scenario: Domain events are drained and not republished
    Given a car generates a CarCreatedEvent
    When drainDomainEvents is called
    Then the event list contains exactly 1 event
    And the internal domainEvents list becomes empty
    And calling drainDomainEvents again returns an empty list

  Scenario: Events are published only after commit
    Given a car generates a CarPriceChangedEvent
    When the application service processes the command
    Then the event is published to Spring ApplicationEventPublisher
    And after commit the DomainEventKafkaRelay publishes it to Kafka
