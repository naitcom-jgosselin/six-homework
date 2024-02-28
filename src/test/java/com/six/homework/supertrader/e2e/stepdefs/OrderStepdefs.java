package com.six.homework.supertrader.e2e.stepdefs;

import com.six.homework.supertrader.entities.Order;
import com.six.homework.supertrader.entities.Security;
import com.six.homework.supertrader.entities.User;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.six.homework.supertrader.e2e.TestUtils.toPlainJson;

@SpringBootTest
public class OrderStepdefs extends AbstractStepdefs {

    @When("the user puts a buy order for the security with a price of {int} and a quantity of {int}")
    public void create_buy_order(Integer price, Integer qty) {
        createOrder(context.firstUser.getId(), context.security.getId(), price, qty, "buy", "bo1");
    }

    @Then("the buy order is created")
    public void the_order_is_created() {
        Order order = context.orders.get("bo1");
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8081/orders/" + order.getId())
                .header("Content-type", "application/json")
                .asJson();

        assert(response.getStatus() == 200);
    }

    @When("the user puts a sell order for the security with a price of {int} and a quantity of {int}")
    public void create_sell_order(Integer price, Integer qty) {
        createOrder(context.firstUser.getId(), context.security.getId(), price, qty, "sell", "so1");
    }

    @Then("the sell order is created")
    public void the_sell_order_is_created() {
        Order order = context.orders.get("so1");
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8081/orders/" + order.getId())
                .header("Content-type", "application/json")
                .asJson();

        assert(response.getStatus() == 200);
    }

    @When("{string} puts a buy order for {string} with a price of {int} and a quantity of {int}")
    public void create_buy_order(String userName, String securityName, Integer price, Integer qty) {
        User user = context.users.get(userName);
        Security security = context.security;

        assert(security.getName().equals(securityName));

        createOrder(user.getId(),
                security.getId(),
                price,
                qty,
                "buy",
                "wsgBuyOrder");
    }

    @When("{string} puts a sell order for {string} with a price of {int} and a quantity of {int}")
    public void create_sell_order(String userName, String securityName, Integer price, Integer qty){
        User user = context.users.get(userName);
        Security security = context.security;

        assert(security.getName().equals(securityName));

        createOrder(user.getId(),
                security.getId(),
                price,
                qty,
                "sell",
                "wsgSellOrder");
    }

    private void createOrder(Long userId, Long securityId, Integer price, Integer qty, String type, String key) {
        // This step assumes that we have a User and Security in memory
        assert(context.security != null);
        assert(context.firstUser != null);

        Order order = new Order(userId,
                securityId,
                type,
                BigDecimal.valueOf(price),
                qty
        );

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8081/orders")
                .header("Content-type", "application/json")
                .body(toPlainJson(order))
                .asJson();

        assert(response.getStatus() == 200);

        order.setId(response.getBody().getObject().getLong("id"));

        context.orders.put(key, order);
    }
}
