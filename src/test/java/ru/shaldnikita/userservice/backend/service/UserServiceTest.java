package ru.shaldnikita.userservice.backend.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shaldnikita.userservice.backend.entity.ResponseUserModel;
import ru.shaldnikita.userservice.backend.entity.User;
import ru.shaldnikita.userservice.backend.repository.UserRepository;
import ru.shaldnikita.userservice.backend.util.UserUtil;
import ru.shaldnikita.userservice.controller.exceptions.UserAlreadyExistsException;
import ru.shaldnikita.userservice.controller.exceptions.UserNotFoundException;
import ru.shaldnikita.userservice.controller.exceptions.WrongEmailException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author n.shaldenkov on 11.04.2018
 */
@RunWith(SpringRunner.class)
public class UserServiceTest {

    private User testUser;

    @TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @MockBean
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        testUser = new User("Nick", "Snow", LocalDate.of(1998, 01, 29), "nick.snow@yandex.ru", "coolPassword");
        testUser.setPassword(null);

        Mockito.when(repository.findUserByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));

        Mockito.when(repository.findUserByEmail("asd"))
                .thenReturn(Optional.ofNullable(null));

        Mockito.when(repository.save(testUser))
                .thenReturn(testUser);

        Mockito.when(repository.deleteByEmail(testUser.getEmail()))
                .thenReturn(1);
    }

    //get
    @Test
    public void userFound() {
        ResponseUserModel user = userService.findUserByEmail("nick.snow@yandex.ru");

        assertNotNull(user);
        assertEquals(UserUtil.createResponseUserModel(testUser), user);
    }

    @Test(expected = UserNotFoundException.class)
    public void userNotFound() {
        userService.findUserByEmail("asd");
    }

    //put
    @Test(expected = UserAlreadyExistsException.class)
    public void userExistsException() {
        userService.createUser(testUser);
    }

    @Test(expected = WrongEmailException.class)
    public void wrongEmail() {
        testUser.setEmail("asd");
        userService.createUser(testUser);
    }

    @Test(expected = WrongEmailException.class)
    public void emptyEmail() {
        testUser.setEmail(null);
        userService.createUser(testUser);
    }

    @Test
    public void createdSuccessfully() {
        testUser.setEmail("asd@mail.ru");
        User createdUser = userService.createUser(testUser);
        assertEquals(testUser, createdUser);
    }

    //delete

    @Test(expected = UserNotFoundException.class)
    public void cantBeDeletedBecauseNotFound() {
        userService.delete("asd");
    }

    @Test
    public void deletedSuccessfully() {
        userService.delete(testUser.getEmail());
    }
}
