package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserApiService {

    private final UserRepository userRepository;

    public void createUser(final UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public List<UserEntity> getAll() {
        return userRepository.getAll();
    }

    public UserEntity findById(final String id) {
        return userRepository.get(Long.valueOf(id));
    }

    public UserEntity updateUser(final String id, final UserEntity userEntity) {
        return userRepository.updateUser(id, userEntity);
    }

    public void deleteUserById(final Long id) {
        userRepository.deleteUserById(id);
    }
}
