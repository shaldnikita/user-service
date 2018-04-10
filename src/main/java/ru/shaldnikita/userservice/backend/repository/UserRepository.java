package ru.shaldnikita.userservice.backend.repository;

import org.springframework.data.repository.CrudRepository;
import ru.shaldnikita.userservice.backend.entity.User;

import java.util.Collection;
import java.util.Optional;

/**
 * @author n.shaldenkov on 10.04.2018
 */
public interface UserRepository extends CrudRepository<User,Long> {

    Optional<User> findUserByEmail(String email);
    int deleteUserByEmail(String email);
    Collection<User> findAll();
}
