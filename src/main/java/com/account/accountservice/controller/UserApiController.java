/**
 * Mapping in controller.
 * https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
 */

package com.account.accountservice.controller;

import com.account.accountservice.mapper.UserMapper;
import com.account.accountservice.model.UserEntity;
import com.account.accountservice.service.UserApiService;
import com.openapi.accountservice.server.controller.api.UserApi;
import com.openapi.accountservice.server.model.api.UserAccountRs;
import com.openapi.accountservice.server.model.api.UserRegistrationRq;
import com.openapi.accountservice.server.model.api.UserRegistrationRs;
import com.openapi.accountservice.server.model.api.UserRs;
import com.openapi.accountservice.server.model.api.UserUpdateRq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserApiController implements UserApi {
    private final UserApiService userApiService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<UserRegistrationRs> createUser(final UserRegistrationRq userRegistrationRq) {
        final UserEntity userEntity = userMapper.mapToEntity(userRegistrationRq);
        userApiService.createUser(userEntity);
        final UserRegistrationRs userRegistrationRs = userMapper.mapToUserRegistrationRs(userEntity);
        return ResponseEntity.ok(userRegistrationRs);
    }

    @Override
    public ResponseEntity<List<UserRs>> getAllUsers() {
        final List<UserRs> userRsList = userApiService.getAll().stream()
            .map(userMapper::mapToUserRs)
            .toList();
        return ResponseEntity.ok(userRsList);
    }

    @Override
    public ResponseEntity<UserRs> getUserById(final String id) {
        final UserEntity userEntity = userApiService.findById(id);
        final UserRs userRs = userMapper.mapToUserRs(userEntity);
        return ResponseEntity.ok(userRs);
    }

    @Override
    public ResponseEntity<UserUpdateRq> updateUser(final String id, final UserUpdateRq userUpdateRq) {
        final UserEntity userEntity = userApiService.updateUser(id, userUpdateRq);
        final UserUpdateRq updateRq = userMapper.mapToUserUpdateRq(userEntity);
        return ResponseEntity.ok(updateRq);
    }

    @Override
    public ResponseEntity<Void> deleteUserById(final String id) {
        userApiService.deleteUserById(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<UserAccountRs> findUserAccounts(final String id) {
        return ResponseEntity.ok(userApiService.findUserAccountInfo(id));
    }
}
