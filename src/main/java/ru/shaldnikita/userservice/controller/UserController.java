package ru.shaldnikita.userservice.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.shaldnikita.userservice.backend.entity.User;
import ru.shaldnikita.userservice.backend.service.UserService;

import javax.persistence.RollbackException;
import java.net.URI;
import java.util.Collection;

/**
 * @author n.shaldenkov on 10.04.2018
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService service;

    /**
     * @param user
     * @return New user location or 422 code if user is not valid
     */
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            User result = service.createUser(user);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (TransactionSystemException e) {
            log.error("Wrong entity");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/email/{email}")
    public @ResponseBody
    User getUserByEmail(@PathVariable String email) {
        return service.findUserByEmail(email);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    User getUserById(@PathVariable Long id) {
        return service.findUserById(id);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
