package ru.shaldnikita.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.shaldnikita.userservice.backend.entity.ResponseUserModel;
import ru.shaldnikita.userservice.backend.entity.User;
import ru.shaldnikita.userservice.backend.service.UserService;
import ru.shaldnikita.userservice.controller.exceptions.UserNotFoundException;
import ru.shaldnikita.userservice.controller.exceptions.UserNotValidException;

import javax.validation.*;
import java.net.URI;
import java.util.Set;

/**
 * @author n.shaldenkov on 10.04.2018
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity addUser(@RequestBody User user) {
        log.info("trying to add user");
        validateUser(user);

        User result = service.createUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{email}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{email}")
    public ResponseUserModel getUser(@PathVariable String email) {
        return service.findUserByEmail(email);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity deleteUser(@PathVariable String email) {
        service.delete(email);
        return ResponseEntity.ok().build();
    }


    private void validateUser(User object) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator
                .validate(object);

        if (constraintViolations.size() > 0)
            buildResponseMessageAndThrowException(object, constraintViolations);

    }

    private void buildResponseMessageAndThrowException(User object, Set<ConstraintViolation<Object>> constraintViolations) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("User [%s] is not valid!", object.toString()))
                .append(String.format(" Got %d errors:", constraintViolations.size()));

        for (ConstraintViolation<Object> cv : constraintViolations)
            sb.append(String.format(" Property: [%s], value: [%s], message: [%s]",
                    cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));

        throw new UserNotValidException(sb.toString());
    }

}
