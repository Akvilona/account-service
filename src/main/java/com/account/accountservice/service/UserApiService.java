package com.account.accountservice.service;

import com.account.accountservice.exception.ErrorCode;
import com.account.accountservice.exception.ServiceException;
import com.account.accountservice.mapper.UserMapper;
import com.account.accountservice.model.UserEntity;
import com.account.accountservice.repository.UserRepository;
import com.openapi.accountservice.server.model.api.UserAccountRs;
import com.openapi.accountservice.server.model.api.UserUpdateRq;
import com.openapi.fraudservice.client.api.FraudUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserApiService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FraudUserApi fraudUserApi;

    public void createUser(final UserEntity userEntity) {
        Boolean result = fraudUserApi.checkFraudUserByEmail(userEntity.getEmail()).block();
        if (Boolean.TRUE.equals(result)) {
            log.info("User with email {} in fraud list!", userEntity.getEmail());
        } else {
            userRepository.save(userEntity);
        }
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(final String id) {
        return userRepository.findById(Long.valueOf(id))
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity updateUser(final String id, final UserUpdateRq userUpdateRq) {
        final UserEntity user = userRepository.findById(Long.parseLong(id))
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, id));

        userMapper.updateWithUserUpdateRq(user, userUpdateRq);

        return user;
    }

    public void deleteUserById(final Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UserAccountRs findUserAccountInfo(final String id) {
        return userRepository.findUserAccountInfo(Long.parseLong(id))
            .map(userMapper::mapToUserAccountRs)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, id));
    }
}
