package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gmm.demo.mapper.UserMapper;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.UserUpdateRq;
import ru.gmm.demo.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserApiService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void createUser(final UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(final String id) {
        return userRepository.findById(Long.valueOf(id))
            .orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity updateUser(final String id, final UserUpdateRq userUpdateRq) {
        UserEntity user = userRepository.findById(Long.parseLong(id))
            .orElseThrow();

        userMapper.updateWithUserUpdateRq(user, userUpdateRq);

        return user;
    }

    public void deleteUserById(final Long id) {
        userRepository.deleteById(id);
    }
}
