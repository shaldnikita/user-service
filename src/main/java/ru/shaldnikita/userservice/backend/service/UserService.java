package ru.shaldnikita.userservice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaldnikita.userservice.backend.entity.User;
import ru.shaldnikita.userservice.backend.repository.UserRepository;
import ru.shaldnikita.userservice.controller.exceptions.UserAlreadyExistsException;
import ru.shaldnikita.userservice.controller.exceptions.UserNotFoundException;

import java.util.Collection;

/**
 * @author n.shaldenkov on 10.04.2018
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        boolean exists = userRepository.findUserByEmail(user.getEmail()).isPresent();
        if (exists)
            throw new UserAlreadyExistsException(user.getEmail());

        user.setPassword(user.getPassword());

        return userRepository.save(user);
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        findUserById(id);
        userRepository.deleteById(id);
    }

    public void deleteByEmail(String email) {
        findUserByEmail(email);
        userRepository.deleteUserByEmail(email);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
