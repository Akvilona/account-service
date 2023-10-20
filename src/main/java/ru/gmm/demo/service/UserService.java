package ru.gmm.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gmm.demo.controller.api.UserApiDelegate;
import ru.gmm.demo.model.api.UserRs;
import ru.gmm.demo.model.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserApiDelegate {
    private final List<User> userRsList = new ArrayList<>();

    @Override
    public ResponseEntity<UserRs> get(final String id) {
        return userRsList.stream()
            .filter(userRs -> userRs.getId().equals(Long.parseLong(id)))
            .findAny()
            .map(user -> UserRs.builder()
                .id(user.getId().intValue())
                .name(user.getName())
                .surname(user.getSurname())
                .build())
            .map(ResponseEntity::ok)
            .orElseThrow();
    }
}
