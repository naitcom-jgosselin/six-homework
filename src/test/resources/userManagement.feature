Feature: User Management
  This is NOT FULLY IMPLEMENTED, as authentication is not yet ready
  This means user creation works, but login is more or less useless

  Scenario: Nominal register/login flow
    Given a new user willing to test our application
    When the user registers
    Then he is then able to login

  Scenario: Logging in with the wrong password
    Given a new user
    When the user registers
    And he tries to log in, but with a wrong password
    Then he receives an error message

  Scenario: Registering a duplicated user name
    Given a new user
    When the user registers
    And mistakenly registers again
    Then the second register attempt is prevented