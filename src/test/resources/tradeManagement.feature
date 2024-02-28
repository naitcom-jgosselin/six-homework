Feature: Trade management
  We chose not to allow the user to manually create trades.
  Instead, if an order is created, it will be resolved automatically against the potential matching orders,
  which may or may not result in new trades being added

  Thus, the only endpoint available is an endpoint which allows to visualize all trades

  Scenario: Fetching trades
    When a user wants to access a list of the trades
    Then he should get a list of trades