package com.account.accountservice.mapper;

import com.account.accountservice.model.AccountEntity;
import com.account.accountservice.model.UserEntity;
import com.openapi.accountservice.server.model.api.AccountRegistrationRq;
import com.openapi.accountservice.server.model.api.AccountRegistrationRs;
import com.openapi.accountservice.server.model.api.AccountRs;
import com.openapi.accountservice.server.model.api.AccountUpdateRq;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    config = MapperConfiguration.class,
    uses = TransactionMapper.class
)
public interface AccountMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "number", source = "accRegistrationRq.account")
    @Mapping(target = "sum", source = "accRegistrationRq.sum")
    AccountEntity updateAccountEntity(AccountRegistrationRq accRegistrationRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "number", source = "accountUpdateRq.account")
    @Mapping(target = "sum", source = "accountUpdateRq.sum")
    @Mapping(target = "status", source = "accountUpdateRq.status")
    void updateAccountEntity(@MappingTarget AccountEntity accountEntity, AccountUpdateRq accountUpdateRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "user", source = "userEntity")
    @Mapping(target = "number", source = "request.account")
    @Mapping(target = "status", constant = "OPENED")
    @Mapping(target = "sum", source = "request.sum")
    AccountEntity toAccountEntityAndUserEntity(AccountRegistrationRq request, UserEntity userEntity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "accountEntity.id")
    @Mapping(target = "sum", source = "accountEntity.sum")
    AccountRegistrationRs toAccountRegistrationRs(AccountEntity accountEntity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "account", source = "number")
    @Mapping(target = "sum", source = "sum")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "transactionsFrom", source = "transactionsFrom")
    @Mapping(target = "transactionsTo", source = "transactionsTo")
    AccountRs toAccountRs(AccountEntity accountEntity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "account", source = "accountEntity.number")
    @Mapping(target = "sum", source = "accountEntity.sum")
    @Mapping(target = "status", source = "accountEntity.status")
    AccountUpdateRq toAccountUpdateRq(AccountEntity accountEntity);
}
