package ru.gmm.demo.repository;

import org.springframework.stereotype.Component;
import ru.gmm.demo.exception.ErrorCode;
import ru.gmm.demo.exception.ServiceException;
import ru.gmm.demo.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListUserRepository {
    private final List<UserEntity> userEntities = new ArrayList<>();

    public void save(final UserEntity user) {
        userEntities.add(user);
    }

    public UserEntity get(final Long id) {
        return findById(id);
    }

    public UserEntity updateUser(final String id, final UserEntity userEntity) {
        final UserEntity entity = findById(Long.parseLong(id));
        return entity
            .setName(userEntity.getName())
            .setSurname(userEntity.getSurname());
    }

    public List<UserEntity> getAll() {
        return userEntities;
    }

    public boolean deleteUserById(final Long id) {
        return userEntities.removeIf(userEntity -> userEntity.getId().equals(id));
    }

    private UserEntity findById(final Long id) {
        return userEntities.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, id));
    }

}
