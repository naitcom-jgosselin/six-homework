package com.six.homework.supertrader.configuration;

import com.six.homework.supertrader.entities.User;
import com.six.homework.supertrader.repositories.SecurityRepository;
import com.six.homework.supertrader.entities.Security;

import com.six.homework.supertrader.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);


    // TODO : we add some datasets for testing/demo purposes. Maybe it's not the state-of-the-art way of doing this.
    @Bean
    CommandLineRunner initDatabase(SecurityRepository securityRepository, UserRepository userRepository) {
        return args -> log.info("Preloading " +
                securityRepository.save(new Security("WSG")) +
                securityRepository.save(new Security("ABS")) +
                securityRepository.save(new Security("BCD")) +
                userRepository.save(new User("Diamond", "azerty")) +
                userRepository.save(new User("Paper", "azerty")));
    }

}
