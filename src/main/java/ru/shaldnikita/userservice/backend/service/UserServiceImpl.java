package ru.shaldnikita.userservice.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaldnikita.userservice.backend.entity.ResponseUserModel;
import ru.shaldnikita.userservice.backend.entity.User;
import ru.shaldnikita.userservice.backend.repository.UserRepository;
import ru.shaldnikita.userservice.backend.util.UserUtil;
import ru.shaldnikita.userservice.controller.exceptions.UserAlreadyExistsException;
import ru.shaldnikita.userservice.controller.exceptions.UserNotFoundException;
import ru.shaldnikita.userservice.controller.exceptions.WrongEmailException;

/**
 * @author n.shaldenkov on 11.04.2018
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        boolean exists = userRepository.findUserByEmail(user.getEmail()).isPresent();
        if (exists)
            throw new UserAlreadyExistsException(user.getEmail());

        user.setPassword(user.getPassword());
        return userRepository.save(user);

    }

    public void delete(String email) {
        findUserByEmail(email);
        userRepository.deleteByEmail(email);
    }


    public ResponseUserModel findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        ResponseUserModel responseUserModel = UserUtil.createResponseUserModel(user);
        return responseUserModel;
    }

}
