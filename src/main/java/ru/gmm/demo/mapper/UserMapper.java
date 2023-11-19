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

    UserRegistrationRs mapToUserRegistrationRs(UserEntity userEntity);

    UserRs mapToUserRs(UserEntity userEntity);

    UserUpdateRq mapToUserUpdateRq(UserEntity userEntity);

    UserAccountRs mapToUserAccountRs(UserEntity userEntity);
}
