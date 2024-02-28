package com.six.homework.supertrader.e2e.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.six.homework.supertrader.entities.Security;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.List;
import java.util.UUID;

import static com.six.homework.supertrader.e2e.TestUtils.toPlainJson;
import static org.junit.jupiter.api.Assertions.fail;

public class SecurityStepdefs extends AbstractStepdefs {

    @When("a user wants to see all the available securities")
    public void a_user_wants_to_see_all_the_available_securities() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8081/securities")
                .header("Content-type", "application/json")
                .asJson();

        context.lastResponseAsString = response.getBody().toPrettyString();
    }

    @Then("he gets a list of securities")
    public void he_gets_a_list_of_securities() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Security> parsedSecurities =
                    objectMapper.readValue(context.lastResponseAsString, new TypeReference<>() {});
            assert(!parsedSecurities.isEmpty());
        } catch (JsonProcessingException e) {
            fail("Unable to parse securities from endpoint");
        }
    }

    @Given("any security")
    @When("a user wants to create a new security")
    public void a_user_wants_to_create_a_new_security() {
        context.security = new Security("TestSecurity" + UUID.randomUUID());

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8081/securities")
                .header("Content-type", "application/json")
                .body(toPlainJson(context.security))
                .asJson();

        context.security.setId(response.getBody().getObject().getLong("id"));

        assert(response.getStatus() == 200);
    }

    @Then("a new security is created")
    public void a_new_security_is_created() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8081/securities")
                .header("Content-type", "application/json")
                .asJson();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Security> parsedSecurities =
                    objectMapper.readValue(response.getBody().toString(), new TypeReference<>() {});
            assert(!parsedSecurities.isEmpty());

            boolean correspondingSecurityFound = false;
            for(Security s : parsedSecurities) {
                if(s.getName().equals(context.security.getName())) {
                    correspondingSecurityFound = true;
                    break;
                }
            }
            assert(correspondingSecurityFound);
        } catch (JsonProcessingException e) {
            fail("Unable to parse securities from endpoint");
        }
    }

    @When("a user tries to create a new security with a duplicated name")
    public void a_user_tries_to_create_a_new_security_with_a_duplicated_name() {
        // Write code here that turns the phrase above into concrete actions
        context.security = new Security("TestSecurity" + UUID.randomUUID());

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8081/securities")
                .header("Content-type", "application/json")
                .body(toPlainJson(context.security))
                .asJson();

        assert(response.getStatus() == 200);

        HttpResponse<JsonNode> secondResponse = Unirest.post("http://localhost:8081/securities")
                .header("Content-type", "application/json")
                .body(toPlainJson(context.security))
                .asJson();

        context.lastStatusCode = secondResponse.getStatus();
    }
    @Then("the security creation should fail")
    public void the_security_creation_should_fail() {
        assert(context.lastStatusCode == 400);
    }

    @Given("the {string} security")
    public void find_or_create_security(String name) {
        // These securities are pre-loaded in configuration.
        ObjectMapper objectMapper = new ObjectMapper();

        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8081/securities")
                .header("Content-type", "application/json")
                .asJson();
        try {
            List<Security> parsedSecurities =
                    objectMapper.readValue(response.getBody().toString(), new TypeReference<>() {});
            for(Security s : parsedSecurities) {
                if(s.getName().equals(name)) {
                    context.security = s;
                    break;
                }
            }

            if(context.security == null) {
                fail(name + ": security not found");
            }
        } catch (JsonProcessingException e) {
            fail("Unable to parse securities from endpoint");
        }
    }
}
