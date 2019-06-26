package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserUtils {
    public static final List<User> USERS = Arrays.asList(
            new User(1, "Dima", "Zhyvodorov", "pswd", Role.ROLE_ADMIN),
            new User(2, "Sasha", "Second", "pswd2", Role.ROLE_USER),
            new User(3, "Anya", "Third", "pswd3", Role.ROLE_USER)
    );

    public static List<User> getUSERS() {
        return USERS.stream().sorted(Comparator.comparing(User::getName)).collect(Collectors.toList());
    }
}
