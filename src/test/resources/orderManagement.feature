Feature: Order management
  This is the most complex part of the app. When a new order is created, we try to match it against existing orders,
  which results into Trades.

  The policies we have are the following :
    - The price is always decided by the seller
    - In case of multiple matching orders, when selling, we try to fulfill the highest bidder first
    - In case of multiple matching orders, when buying, we try to fulfill the lowest bidder first

  Scenario: Create a buy order
    Given any security
    And any user
    When the user puts a buy order for the security with a price of 100 and a quantity of 50
    Then the buy order is created

  Scenario: Create a sell order
    Given any security
    And any user
    When the user puts a sell order for the security with a price of 100 and a quantity of 50
    Then the sell order is created

  Scenario: Nominal trade case
    Given the "WSG" security
    And the user "Diamond"
    And the user "Paper"
    When "Diamond" puts a buy order for "WSG" with a price of 101 and a quantity of 50
    And "Paper" puts a sell order for "WSG" with a price of 100 and a quantity of 100
    Then a corresponding trade occurs with a price of 100 and a quantity of 50