package ru.gmm.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.UserAccountRs;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.model.api.UserRs;
import ru.gmm.demo.model.api.UserUpdateRq;

import java.util.Random;

@Mapper(
    config = MapperConfiguration.class,
    imports = Random.class
)
public interface UserMapper {

    Random RANDOM = new Random();

    @Mapping(target = "id", expression = "java( RANDOM.nextLong() )")
    UserEntity mapToEntity(UserRegistrationRq userRegistrationRq);

    default void updateWithUserUpdateRq(final UserEntity userEntity, final UserUpdateRq userUpdateRq) {
        userEntity.setName(userUpdateRq.getName());
        userEntity.setSurname(userUpdateRq.getSurname());
    }

    default UserRegistrationRs mapToUserRegistrationRs(final UserEntity userEntity) {
        return UserRegistrationRs.builder()
            .id(String.valueOf(userEntity.getId()))
            .email(userEntity.getEmail())
            .build();
    }

    default UserRs mapToUserRs(final UserEntity userEntity) {
        return UserRs.builder()
            .id(String.valueOf(userEntity.getId()))
            .email(userEntity.getEmail())
            .name(userEntity.getName())
            .surname(userEntity.getSurname())
            .build();
    }

    default UserUpdateRq mapToUserUpdateRq(final UserEntity userEntity) {
        return UserUpdateRq.builder()
            .name(userEntity.getName())
            .surname(userEntity.getSurname())
            .build();
    }

    UserAccountRs mapToUserAccountRs(UserEntity userEntity);
}
