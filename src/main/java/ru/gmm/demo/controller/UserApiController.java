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
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.service.UserApiService;

@Controller
@RequiredArgsConstructor
public class UserApiController implements UserApi {
    private   final UserApiService userApiService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<UserRegistrationRs> createUser(final UserRegistrationRq userRegistrationRq) {
        UserEntity userEntity = userMapper.mapToEntity(userRegistrationRq);
        userApiService.createUser(userEntity);
        UserRegistrationRs userRegistrationRs = userMapper.mapToUserRegistrationRs(userEntity);
        UserApi.super.createUser(userRegistrationRq);

        return ResponseEntity.ok(userRegistrationRs);
    }
}
