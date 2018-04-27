package ru.shaldnikita.userservice.backend.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class User {

    @Id
    @Email
    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    @NotEmpty
    private String firstName;
    @NotNull
    @NotEmpty
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
        this.password=password;
    }
}
