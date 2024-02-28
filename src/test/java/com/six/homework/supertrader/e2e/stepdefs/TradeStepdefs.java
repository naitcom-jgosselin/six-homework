package com.six.homework.supertrader.e2e.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.six.homework.supertrader.entities.Order;
import com.six.homework.supertrader.entities.Security;
import com.six.homework.supertrader.entities.Trade;
import io.cucumber.java.en.Then;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class TradeStepdefs extends AbstractStepdefs {
    @Then("a corresponding trade occurs with a price of {int} and a quantity of {int}")
    public void findCorrespondingTrade(Integer price, Integer qty) {
        Order buyOrder = context.orders.get("wsgBuyOrder");
        Order sellOrder = context.orders.get("wsgSellOrder");

        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8081/trades")
                .header("Content-type", "application/json")
                .asJson();

        boolean correspondingTradeFound = false;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Trade> parsedTrades =
                    objectMapper.readValue(response.getBody().toString(), new TypeReference<>() {});
            for(Trade t : parsedTrades) {
                if(t.getPrice().compareTo(BigDecimal.valueOf(price)) == 0 &&
                        t.getQuantity() == qty &&
                        t.getBuyOrderId().equals(buyOrder.getId()) &&
                        t.getSellOrderId().equals(sellOrder.getId())) {
                    correspondingTradeFound = true;
                    break;
                }
            }
            if(!correspondingTradeFound) {
                fail("No corresponding trade found");
            }
        } catch (JsonProcessingException e) {
            fail("Unable to parse trades from endpoint");
        }
    }
}
