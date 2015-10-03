@automation
Feature: Automation

  Scenario: Enable iOS Plugin
    Given Plugin is enabled
    When I export iOS application
    Then iOS application should contain plugin files
    And iOS application should can be built

  Scenario: Enable Android Plugin
    Given Plugin is enabled
    When I export Android application
    Then Android application should contain plugin files

  Scenario: Disable iOS Plugin
    Given Plugin is NOT enabled
    When I export iOS application
    Then iOS application should NOT contain plugin files
    And iOS application should can be built

  Scenario: Disable Android Plugin
    Given Plugin is NOT enabled
    When I export Android application
    Then Android application should NOT contain plugin files

  Scenario: Toggle iOS Plugin
    Given Plugin is NOT enabled
    When I export iOS application
    Then iOS application should NOT contain plugin files
    And iOS application should can be built
    Given Plugin is enabled
    When I export iOS application
    Then iOS application should contain plugin files
    And iOS application should can be built

  Scenario: Toggle Android Plugin
    Given Plugin is NOT enabled
    When I export Android application
    Then Android application should NOT contain plugin files
    Given Plugin is enabled
    When I export Android application
    Then Android application should contain plugin files