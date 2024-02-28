Feature: Security management
  This should make sure that we provide :
    A way to see all securities available
    A way of creating a new security

  Scenario: Fetching all available securities
    When a user wants to see all the available securities
    Then he gets a list of securities

  Scenario: Creating a new security
    When a user wants to create a new security
    Then a new security is created

  Scenario: Creating a new security with a duplicated name
    When a user tries to create a new security with a duplicated name
    Then the security creation should fail