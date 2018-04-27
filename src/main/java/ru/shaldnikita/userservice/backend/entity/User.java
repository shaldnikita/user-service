package ru.shaldnikita.userservice.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author n.shaldenkov on 10.04.2018
 */
@Data
@Entity
@NoArgsConstructor
public class User {

    @Id
    @Email(message = "Wrong email format")
    @NotNull(message = "Email cant be null")
    @NotEmpty(message = "Email cant be empty")
    private String email;
    @NotNull(message = "First name cant be null")
    @NotEmpty(message = "First name cant be empty")
    private String firstName;
    @NotNull(message = "Second name cant be null")
    @NotEmpty(message = "Second name cant be empty")
    private String secondName;
    @NotNull
    private LocalDate birthday;
    @NotNull
    @NotEmpty
    private String password;

    public User(String firstName, String secondName, LocalDate birthday, String email, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
        this.email = email;
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = DigestUtils.sha256Hex(password);
    }
}
