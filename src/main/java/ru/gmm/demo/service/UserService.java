/**
 * Создал Андрей Антонов 10/17/2023 1:26 PM
 **/
package ru.gmm.demo.service;

import org.springframework.stereotype.Service;
import ru.gmm.demo.model.User;
import ru.gmm.demo.model.UserRegistrationRq;
import ru.gmm.demo.model.UserRs;
import ru.gmm.demo.model.UserUpdateRq;

import java.util.List;
import java.util.Random;

@Service
public class UserService {
    private final List<User> userRsList = List.of(
            User.builder()
                    .id(new Random().nextLong())
                    .name("igor")
                    .serName("simakov")
                    .password("12345")
                    .build(),
            User.builder()
                    .id(new Random().nextLong())
                    .name("sasha")
                    .serName("rumanov")
                    .password("12345")
                    .build()
    );

    public List<UserRs> getUsers() {

        return userRsList.stream()
                .map(this::toUserRs)
                .toList();
    }

    public UserRs getUser(String id) {
        // TODO: я не смог повторить запись, поэтому написал простой for и if
        return toUserRs(userRsList.stream().filter(userRs -> userRs.getId().equals(Long.parseLong(id))).findAny().orElseThrow());
    }

    private UserRs toUserRs(User user) {
        return UserRs.builder()
                .id(user.getId())
                .name(user.getName())
                .surName(user.getSerName())
                .build();
    }

    public UserRs registration(UserRegistrationRq userRegistrationRq) {
        User user = User.builder()
                .id(new Random().nextLong())
                .name(userRegistrationRq.getName())
                .password(String.valueOf(new Random().nextLong(1000)))
                .build();

        userRsList.add(user);
        return toUserRs(user);

        // TODO: на строке userRsList.add(user) происходит ошибка
/*        {
            "timestamp": "2023-10-19T08:38:21.800+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/api/users"
        }*/
    }

    public UserRs update(UserUpdateRq userUpdateRq) {
        User forUpdate = userRsList.stream()
                .filter(user -> user.getId().equals(userUpdateRq.getId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("User with id: %s not exist".formatted(userUpdateRq.getId())));

        forUpdate.setName(userUpdateRq.getName());
        forUpdate.setPassword(userUpdateRq.getPassword());

        return toUserRs(forUpdate);
// TODO:  происходит ошибка
/*
        {
            "timestamp": "2023-10-19T09:20:03.206+00:00",
                "status": 400,
                "error": "Bad Request",
                "path": "/api/users"
        }
 */
    }
}
