package ru.shaldnikita.userservice.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserModel {


    private String email;

    private String firstName;
    private String secondName;

    private LocalDate birthday;
}
