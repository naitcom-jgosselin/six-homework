package com.six.homework.supertrader.e2e.stepdefs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class AbstractStepdefs {
    @Autowired
    TestContext context;

    public AbstractStepdefs() {}

}
