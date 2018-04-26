package ru.shaldnikita.userservice.backend.util;

import ru.shaldnikita.userservice.backend.entity.ResponseUserModel;
import ru.shaldnikita.userservice.backend.entity.User;

public class UserUtil {

    public static ResponseUserModel createResponseUserModel(User user) {
        return new ResponseUserModel(user.getEmail(), user.getFirstName(), user.getSecondName(), user.getBirthday());
    }
}
