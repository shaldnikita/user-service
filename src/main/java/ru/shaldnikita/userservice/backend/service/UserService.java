package ru.shaldnikita.userservice.backend.service;

import ru.shaldnikita.userservice.backend.entity.User;

/**
 * @author n.shaldenkov on 11.04.2018
 */

public interface UserService {
    User createUser(User user) ;

    void delete(String email);

    User findUserByEmail(String email);
}
