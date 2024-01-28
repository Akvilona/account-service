package com.account.accountservice.mapper;

import com.account.accountservice.model.UserEntity;
import com.account.accountservice.model.api.UserAccountRs;
import com.account.accountservice.model.api.UserRegistrationRq;
import com.account.accountservice.model.api.UserRegistrationRs;
import com.account.accountservice.model.api.UserRs;
import com.account.accountservice.model.api.UserUpdateRq;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    @Mapping(target = "id", source = "userEntity.id")
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
