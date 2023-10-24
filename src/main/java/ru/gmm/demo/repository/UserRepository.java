package ru.gmm.demo.repository;

import org.springframework.stereotype.Component;
import ru.gmm.demo.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    private final List<UserEntity> userEntities = new ArrayList<>();

    public void save(final UserEntity user) {
        userEntities.add(user);
    }

    public UserEntity get(final Long id) {
        return userEntities.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Cant find uer by id %s".formatted(id)));
    }
}
