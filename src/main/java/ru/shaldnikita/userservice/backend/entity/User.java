package ru.shaldnikita.userservice.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author n.shaldenkov on 10.04.2018
 */
@Data
//@JsonIgnoreProperties("password")
@Entity
@NoArgsConstructor
public class User {
    /**
     * Фамилия, Имя, Дата рождения, email и
     * пароль;
     */
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String secondName;

    private LocalDate birthday;

    @Email
    private String email;

    private String password;

    public User(String firstName, String secondName, LocalDate birthday, @Email String email, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
        this.email = email;
        if (password != null)
            setPassword(password);
    }

    public void setPassword(String password) {
        this.password = DigestUtils.sha256Hex(password);
    }
}
