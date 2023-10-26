package ru.gmm.demo.mapper;

import org.springframework.stereotype.Component;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;

import java.util.Random;

@Component
public class UserMapper {
    public static final Random RANDOM = new Random();

    public UserEntity mapToEntity(final UserRegistrationRq userRegistrationRq) {
        return UserEntity.builder()
            .id(RANDOM.nextLong())
            .password(userRegistrationRq.getPassword())
            .email(userRegistrationRq.getEmail())
            .build();
    }

    public UserRegistrationRs mapToUserRegistrationRs(final UserEntity userEntity) {
        return UserRegistrationRs.builder()
            .id(userEntity.getId().intValue())
            .email(userEntity.getEmail())
            .build();
    }
}
