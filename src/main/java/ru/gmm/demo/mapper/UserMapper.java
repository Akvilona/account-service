package ru.gmm.demo.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.UserAccountRs;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.model.api.UserRs;
import ru.gmm.demo.model.api.UserUpdateRq;

import java.util.Random;

@Mapper(
    config = MapperConfiguration.class,
    uses = AccountMapper.class
)
public interface UserMapper {

    Random RANDOM = new Random();

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "id", expression = "java( RANDOM.nextLong() )")
    UserEntity mapToEntity(UserRegistrationRq userRegistrationRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "userUpdateRq.name")
    @Mapping(target = "surname", source = "userUpdateRq.surname")
    void updateWithUserUpdateRq(@MappingTarget UserEntity userEntity, UserUpdateRq userUpdateRq);

    UserRegistrationRs mapToUserRegistrationRs(UserEntity userEntity);

    UserRs mapToUserRs(UserEntity userEntity);

    UserUpdateRq mapToUserUpdateRq(UserEntity userEntity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "surname", source = "surname")
    @Mapping(target = "accounts", source = "accounts")
    UserAccountRs mapToUserAccountRs(UserEntity userEntity);
}
