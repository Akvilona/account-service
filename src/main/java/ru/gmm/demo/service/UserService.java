/**
 * Создал Андрей Антонов 10/17/2023 1:26 PM
 **/
package ru.gmm.demo.service;

import org.springframework.stereotype.Service;
import ru.gmm.demo.model.User;
import ru.gmm.demo.model.UserRegistrationRq;
import ru.gmm.demo.model.UserRs;
import ru.gmm.demo.model.UserUpdateRq;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserService {
    private final List<User> userRsList = new ArrayList<>();

    public List<UserRs> getUsers() {

        return userRsList.stream()
                .map(this::toUserRs)
                .toList();
    }

    public UserRs getUser(String id) {
        User user = userRsList.stream()
                .filter(userRs -> userRs.getId().equals(Long.parseLong(id)))
                .findAny()
                .orElseThrow();
        return toUserRs(user);
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
    }

    public UserRs update(UserUpdateRq userUpdateRq) {
        User forUpdate = userRsList.stream()
                .filter(user -> user.getId().equals(userUpdateRq.getId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("User with id: %s not exist".formatted(userUpdateRq.getId())));

        forUpdate.setName(userUpdateRq.getName());
        forUpdate.setPassword(userUpdateRq.getPassword());

        return toUserRs(forUpdate);
    }

    public void delete(long id) {
        User user1 = userRsList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow();
        userRsList.remove(user1);
    }
}
