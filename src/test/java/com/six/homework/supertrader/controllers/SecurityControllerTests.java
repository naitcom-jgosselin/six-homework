package com.six.homework.supertrader.controllers;

import com.six.homework.supertrader.controllers.security.SecurityAlreadyExistsException;
import com.six.homework.supertrader.controllers.security.SecurityController;
import com.six.homework.supertrader.entities.Security;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SecurityControllerTests {
    @Autowired
    SecurityController securityController;

    @Test
    public void createSecurityNominal() {
        Security security = new Security("Bread");
        Security createdSecurity = securityController.createSecurity(security);

        // On creation security receives an ID
        security.setId(createdSecurity.getId());

        assert(security.equals(createdSecurity));
    }

    @Test
    public void shouldNotAllowDuplicatedNames() {
        Security security = new Security("Rice");
        securityController.createSecurity(security);

        assertThrows(SecurityAlreadyExistsException.class,
                () -> securityController.createSecurity(security));
    }
}
