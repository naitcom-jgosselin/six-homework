# SIX Code Challenge

This is a proposed solution to the SIX Code Challenge that was given to me on the 21th of February.

The solution consists in:
* a barebones Springboot application which will serve as our System Under Test
* a suite of Unit Tests (which were not required, but I needed them)
* a suite of Cucumber E2E tests

## The application

The application is a very rudimentary Springboot RestApi, which allows the management of the following entities :
* Securities
* Users
* Orders
* Trade

For the challenge, I chose to keep the API as minimalistic as possible.

Here is how it works:
* The user is allowed to create Users, and Securities 
* He can use these to create Orders, which associates a User and a Security to a Price, OrderType and Quantity
* When an order is created, the API will check whether it is possible to create Trades.
* Trades are created automatically when two orders are found to be a match.

Here is the trade matching policy:
* We match buy orders with sell orders of the same security which have a price lower or equal to the order
* We match sell orders with buy orders of the same security which have a price greater or equal to the order
* The price of the resulting trade is always the seller's price
* When multiple matches, we first match sell orders with the lowest bidder, and buy orders with the highest bidder

## The tests

### Unit testing

Unit testing is not a part of what was asked for the challenge, but I needed them to be sure of my code.

If you are curious and want to run them :

For UNIX systems:
> ./gradlew test

For Windows systems:
> .\gradlew.bat test

### E2E testing - Cucumber

A quick word on how this works. I've tried to be smart and come up with a completely stand-alone one-liner command.
This ended up working, but it's a bit dirty.

That means when you run this command, the framework will:
* Run an instance of the application on the port 8081 in the before_all hook
* Run the tests, using localhost:8081, but otherwise treating the API as a complete black-box
* Shutdown the application when done in the after_all hook
* Generate a report, located in the target folder

To run the E2E tests:

For UNIX systems:
> ./gradlew cucumberCli

For Windows systems:
> .\gradlew.bat cucumberCli 

I've included an example of a report in the root of the project 

