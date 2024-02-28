package com.six.homework.supertrader;

import com.six.homework.supertrader.entities.Security;
import com.six.homework.supertrader.entities.User;
import com.six.homework.supertrader.repositories.OrderRepository;
import com.six.homework.supertrader.repositories.SecurityRepository;
import com.six.homework.supertrader.repositories.TradeRepository;
import com.six.homework.supertrader.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public abstract class AbstractTestClass {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityRepository securityRepository;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    OrderRepository orderRepository;

    // We need this to prevent tests from interfering with each other
    @BeforeEach
    public void clearOrdersAndTrades() {
        tradeRepository.deleteAll();
        orderRepository.deleteAll();
    }

    protected User findAnyUser() {
        if(userRepository.count() == 0) {
            User freshUser = new User("Alain", "azerty");
            userRepository.save(freshUser);
        }
        return userRepository.findAll().get(0);
    }

    protected User findUserByNameOrCreateIt(String name) {
        Optional<User> optUser = userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(name))
                .findFirst();

        return optUser.orElseGet(() -> userRepository.save(new User(name, "azerty")));
    }

    protected Security findAnySecurity() {
        if(securityRepository.count() == 0) {
            Security freshSecurity = new Security("ACME");
            securityRepository.save(freshSecurity);
        }
        return securityRepository.findAll().get(0);
    }
}
