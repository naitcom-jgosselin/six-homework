package com.six.homework.supertrader.e2e;

import com.six.homework.supertrader.SupertraderApplication;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;

@CucumberContextConfiguration
@SpringBootTest
public class CucumberSpringConfiguration {

    private static SpringApplication app;
    private static ConfigurableApplicationContext cac;
    @BeforeAll
    public static void before_all() {
        app = new SpringApplication(SupertraderApplication.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8081")); // different port so it doesn't collide with 8080

        cac = app.run();
    }

    @AfterAll
    public static void after_all() {
        SpringApplication.exit(cac);
    }
}
