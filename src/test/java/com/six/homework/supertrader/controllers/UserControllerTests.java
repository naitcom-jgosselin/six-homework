package com.six.homework.supertrader.controllers;

import com.six.homework.supertrader.controllers.user.InvalidCredentialsException;
import com.six.homework.supertrader.controllers.user.UserAlreadyExistsException;
import com.six.homework.supertrader.entities.User;
import com.six.homework.supertrader.AbstractTestClass;
import com.six.homework.supertrader.controllers.user.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTests extends AbstractTestClass {

    @Autowired
    UserController userController;

    @Test
    public void createUserNominal() {
        User user = new User("Alain Populaire", "azerty");

        User createdUser = userController.register(user);

        user.setId(createdUser.getId());
        assert(user.equals(createdUser));
    }

    @Test
    public void shouldNotAllowDuplicatedUsernames() {
        User user = new User("Alain Portun", "azerty");

        userController.register(user);

        assertThrows(UserAlreadyExistsException.class, () -> userController.register(user));
    }

    @Test
    public void loginNominal() {
        User user = findUserByNameOrCreateIt("Diamond");

        assert(user.equals(userController.login("Diamond", "azerty")));
    }

    @Test
    public void loginWithWrongPasswordShouldRaiseException() {
        assertThrows(InvalidCredentialsException.class,
                () -> userController.login("Paper", "qwerty"));
    }

}
