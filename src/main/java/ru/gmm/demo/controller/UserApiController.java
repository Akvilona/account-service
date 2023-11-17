/**
 * Mapping in controller.
 * https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
 */

package ru.gmm.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.gmm.demo.controller.api.UserApi;
import ru.gmm.demo.mapper.UserMapper;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.UserAccountRs;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.model.api.UserRs;
import ru.gmm.demo.model.api.UserUpdateRq;
import ru.gmm.demo.service.UserApiService;

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
