package ru.shaldnikita.userservice.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.Email;

/**
 * @author n.shaldenkov on 11.04.2018
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class WrongEmailException extends RuntimeException {
    public WrongEmailException() {
        super("Email is wrong or empty");
    }
}
