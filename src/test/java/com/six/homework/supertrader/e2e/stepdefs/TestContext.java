package com.six.homework.supertrader.e2e.stepdefs;

import com.six.homework.supertrader.entities.Order;
import com.six.homework.supertrader.entities.Security;
import com.six.homework.supertrader.entities.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;

// This massive code smell of a class serves as a data holder
// I do this because time is running short
@Component
public class TestContext {
    int lastStatusCode;
    String lastResponseAsString;

    Security security;
    User firstUser;

    HashMap<String, User> users = new HashMap<>();
    HashMap<String, Order> orders = new HashMap<>();

    public TestContext() {

    }
}
