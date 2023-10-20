package ru.gmm.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gmm.demo.controller.api.UserApiDelegate;
import ru.gmm.demo.model.dto.UserRegistrationRq;
import ru.gmm.demo.model.dto.UserRs;
import ru.gmm.demo.model.dto.UserUpdateRq;
import ru.gmm.demo.model.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserService implements UserApiDelegate {
    private final List<User> userRsList = new ArrayList<>();

    @Override
    public ResponseEntity<ru.gmm.demo.model.api.UserRs> get(String id) {
        final User user = userRsList.stream()
            .filter(userRs -> userRs.getId().equals(Long.parseLong(id)))
            .findAny()
            .orElseThrow();

        ru.gmm.demo.model.api.UserRs userRs = new ru.gmm.demo.model.api.UserRs();
        userRs.setId(user.getId().intValue());
        userRs.setName(user.getName());
        userRs.setSurname(user.getSurname());

        return ResponseEntity.ok(userRs);
    }

    public List<UserRs> getUsers() {

        return userRsList.stream()
            .map(this::toUserRs)
            .toList();
    }

    public UserRs getUser(final String id) {
        final User user = userRsList.stream()
            .filter(userRs -> userRs.getId().equals(Long.parseLong(id)))
            .findAny()
            .orElseThrow();
        return toUserRs(user);
    }

    private UserRs toUserRs(final User user) {
        return UserRs.builder()
            .id(user.getId())
            .name(user.getName())
            .surName(user.getSurname())
            .build();
    }

    public UserRs registration(final UserRegistrationRq userRegistrationRq) {
        final User user = User.builder()
            .id(new Random().nextLong())
            .name(userRegistrationRq.getName())
            .password(String.valueOf(new Random().nextLong(1000)))
            .build();

        userRsList.add(user);
        return toUserRs(user);
    }

    public UserRs update(final UserUpdateRq userUpdateRq) {
        final User forUpdate = userRsList.stream()
            .filter(user -> user.getId().equals(userUpdateRq.getId()))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("User with id: %s not exist".formatted(userUpdateRq.getId())));

        forUpdate.setName(userUpdateRq.getName());
        forUpdate.setPassword(userUpdateRq.getPassword());

        return toUserRs(forUpdate);
    }

    public void delete(final long id) {
        final User user1 = userRsList.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElseThrow();
        userRsList.remove(user1);
    }
}
