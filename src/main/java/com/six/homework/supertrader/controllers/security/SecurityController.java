package com.six.homework.supertrader.controllers.security;

import com.six.homework.supertrader.entities.Security;
import com.six.homework.supertrader.repositories.SecurityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SecurityController {

    private final SecurityRepository repository;

    SecurityController(SecurityRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/securities")
    public Security createSecurity(@RequestBody Security security) {
        List<Security> securities = repository.findAll();

        for(Security s : securities) {
            if(s.getName().equals(security.getName())) {
                throw new SecurityAlreadyExistsException("There is already a security by that name");
            }
        }
        return repository.save(security);
    }

    @GetMapping("/securities")
    public List<Security> getAllSecurities() {
        return repository.findAll();
    }

    @GetMapping("/securities/{id}")
    public Security getSecurity(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SecurityNotFoundException("No such security - id : " + id));
    }
}
