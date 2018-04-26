package ru.shaldnikita.userservice.backend.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
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
    private String email;

    @NotNull
    private String firstName;
    @NotNull
    private String secondName;
    @NotNull
    private LocalDate birthday;

    @NotNull
    private String password;

    public User(String firstName, String secondName, LocalDate birthday, String email, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
        this.email = email;
        if (password != null)
            setPassword(password);
    }

    public void setPassword(String password) {
        if (password != null)
            this.password = DigestUtils.sha256Hex(password);
        else
            this.password = password;
    }
}
