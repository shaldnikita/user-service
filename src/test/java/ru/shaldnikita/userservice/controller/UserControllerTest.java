package ru.shaldnikita.userservice.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import ru.shaldnikita.userservice.UserServiceApplication;
import ru.shaldnikita.userservice.backend.entity.User;
import ru.shaldnikita.userservice.backend.repository.UserRepository;
import ru.shaldnikita.userservice.backend.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * @author n.shaldenkov on 10.04.2018
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class)
@WebAppConfiguration
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<User> users;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        users = new ArrayList<>();

        userRepository.deleteAll();

        User testUser = new User("Nick", "Snow", LocalDate.of(1998, 01, 29), "nick.snow@yandex.ru", "coolPassword");
        users.add(userService.createUser(testUser));

        testUser = new User("Alex", "Brown", LocalDate.of(2000, 03, 23), "alex.brown@yandex.ru", "secure");
        users.add(userService.createUser(testUser));

        testUser = new User("Donald", "Trump", LocalDate.of(1970, 8, 03), "donald.trump@yandex.ru", "redButton1");
        users.add(userService.createUser(testUser));
    }

    @Test
    public void findAllTest() {

    }

    @Test
    public void userCreatedSuccessfully() throws Exception {
        String userJson = json(new User("firstName", "secondName",
                LocalDate.now(), "test@yandex.ru", "coolPassword321"));

        mockMvc.perform(post("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void cantCreateWithWrongEmail() throws Exception {
        String userJson = json(new User("firstName", "secondName",
                LocalDate.now(), "asdasd", "coolPassword321"));

        mockMvc.perform(post("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isNoContent());
    }

    @Test
    public void findWithIdFoundSuccessfully() throws Exception {
        mockMvc.perform(get("/users/" + users.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(this.json(users.get(0))));
    }

    @Test
    public void userNotFoundWithId() throws Exception {
        mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void userWithEmailFoundSuccessfully() throws Exception {
        mockMvc.perform(get("/users/email/" + users.get(0).getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(this.json(users.get(0))));
    }

    @Test
    public void userNotFoundWithEmail() throws Exception {
        mockMvc.perform(get("/users/email/asd")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletedSuccessfully() throws Exception {
        mockMvc.perform(delete("/users/" + users.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void userToBeDeletedNotFound() throws Exception {
        mockMvc.perform(delete("/users/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
