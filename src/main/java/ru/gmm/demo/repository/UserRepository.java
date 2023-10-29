package ru.gmm.demo.repository;

import org.springframework.stereotype.Component;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.UserUpdateRq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            .orElseThrow(() -> new IllegalArgumentException(String.format("Cant find uer by id %s", id)));
    }

        public UserEntity updateUser(final String id, final UserUpdateRq userUpdateRq) {
            Optional<UserEntity> updatedUser = userEntities.stream()
                .filter(userEntity -> userEntity.getId().equals(Long.parseLong(id)))
                .findFirst();

            if (updatedUser.isPresent()) {
                updatedUser.get().setName(userUpdateRq.getName());
                updatedUser.get().setSurname(userUpdateRq.getSurname());
                return updatedUser.get();
            } else {
                throw new IllegalArgumentException(String.format("Cannot find user by id %s", id));
            }
        }

    public List<UserEntity> getAll() {
        return userEntities;
    }

    public boolean deleteUserById(final Long id) {
        return userEntities.removeIf(userEntity -> userEntity.getId().equals(id));
    }
}
