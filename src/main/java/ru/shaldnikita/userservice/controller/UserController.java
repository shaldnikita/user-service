package ru.shaldnikita.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.shaldnikita.userservice.backend.entity.User;
import ru.shaldnikita.userservice.backend.service.UserService;

import java.net.URI;

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
    public ResponseEntity addUser(@RequestBody User user) {
        User result = service.createUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{email}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{email}")
    public @ResponseBody
    User getUser(@PathVariable String email) {
        return service.findUserByEmail(email);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity deleteUser(@PathVariable String email) {
        service.delete(email);
        return ResponseEntity.ok().build();
    }
}
