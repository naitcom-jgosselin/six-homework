package com.six.homework.supertrader.e2e.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.six.homework.supertrader.entities.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.UUID;

import static com.six.homework.supertrader.e2e.TestUtils.toPlainJson;
import static org.assertj.core.api.Fail.fail;

public class UserStepdefs extends AbstractStepdefs {

    @Given("a new user")
    @Given("a new user willing to test our application")
    public void new_user() {
        context.firstUser = new User("TestUser" + UUID.randomUUID(), "azerty");
    }

    @When("the user registers")
    public void the_user_registers() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8081/register")
                .header("Content-type", "application/json")
                .body(toPlainJson(context.firstUser))
                .asJson();

        context.firstUser.setId(response.getBody().getObject().getLong("id"));

        assert(response.getStatus() == 200);
    }

    @Given("any user")
    public void any_user() {
        new_user();
        the_user_registers();
    }

    @Then("he is then able to login")
    public void he_is_then_able_to_login() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8081/login")
                .header("Content-type", "application/json")
                .header("username", context.firstUser.getUsername())
                .header("password", context.firstUser.getPassword())
                .asJson();

        assert(response.getStatus() == 200);
    }

    @When("he tries to log in, but with a wrong password")
    public void he_tries_to_log_in_but_with_a_wrong_password() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8081/login")
                .header("Content-type", "application/json")
                .header("username", context.firstUser.getUsername())
                .header("password", context.firstUser.getPassword() + "a")
                .asJson();

        context.lastStatusCode = response.getStatus();
    }
    @Then("he receives an error message")
    public void he_receives_an_error_message() {
        assert(context.lastStatusCode == 401);
    }


    @When("mistakenly registers again")
    public void mistakenly_registers_again() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8081/register")
                .header("Content-type", "application/json")
                .body(toPlainJson(context.firstUser))
                .asJson();

        context.lastStatusCode = response.getStatus();
    }

    @Then("the second register attempt is prevented")
    public void the_second_register_attempt_is_prevented() {
        assert(context.lastStatusCode == 400);
    }

    @Given("the user {string}")
    public void find_user(String userName) {
        // Login returns you the user. We always use the same password so it's easy to factorize
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8081/login")
                .header("Content-type", "application/json")
                .header("username", userName)
                .header("password", "azerty")
                .asJson();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            context.users.put(userName, objectMapper.readValue(response.getBody().toString(), User.class));
        } catch (JsonProcessingException e) {
            fail("Unable to read user " + userName);
        }

    }


}
