package com.six.homework.supertrader.controllers.user;

import com.six.homework.supertrader.entities.User;
import com.six.homework.supertrader.repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/*
 All of this is more or less PLACEHOLDER
 Authentication/Authorization in Spring is a topic I didn't have the time to go into
 */
@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
       List<User> users = repository.findAll();

       for(User u : users) {
           if(u.getUsername().equals(user.getUsername())) {
               throw new UserAlreadyExistsException("There is already an user by that name");
           }
       }
        return
               repository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestHeader("username") String userName, @RequestHeader("password") String password ) {
        // Find corresponding password
        List<User> users = repository.findAll();

        for(User u : users) {
            if(u.getUsername().equals(userName) && u.getPassword().equals(password)) {
                return u;
            }
        }
        throw new InvalidCredentialsException("Invalid username or password");
    }

}
