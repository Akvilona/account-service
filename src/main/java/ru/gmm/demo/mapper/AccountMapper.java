/**
 * Создал Андрей Антонов 11/1/2023 9:03 AM.
 **/

package ru.gmm.demo.mapper;

import org.springframework.stereotype.Component;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.AccountRegistrationRq;
import ru.gmm.demo.model.api.AccountRegistrationRs;
import ru.gmm.demo.model.api.AccountRs;
import ru.gmm.demo.model.api.AccountUpdateRq;

import java.util.Random;

@Component
public class AccountMapper {
    public static final Random RANDOM = new Random();

    public AccountEntity toAccountEntity(final AccountRegistrationRq accRegistrationRq) {
        return AccountEntity.builder()
            .id(RANDOM.nextLong())
            .number(accRegistrationRq.getAccount())
            .sum(accRegistrationRq.getSum())
            .build();
    }

    public AccountEntity toAccountEntity(final AccountEntity accountEntity, final AccountUpdateRq accountUpdateRq) {
        accountEntity.setNumber(accountUpdateRq.getAccount());
        accountEntity.setSum(accountUpdateRq.getSum());
        return accountEntity;
    }

    public AccountEntity toAccountEntityAndUserEntity(final AccountEntity accountEntity, final UserEntity userEntity) {
        return AccountEntity.builder()
            .id(RANDOM.nextLong())
            .user(userEntity)
            .number(accountEntity.getNumber())
            .status(accountEntity.getStatus())
            .sum(accountEntity.getSum())
            .build();
    }

    public AccountRegistrationRs toAccountRegistrationRs(final AccountEntity accountEntity) {
        return AccountRegistrationRs.builder()
            .id(String.valueOf(accountEntity.getId()))
            .sum(accountEntity.getSum())
            .build();
    }

    public AccountRs mapToAccRs(final AccountEntity accountEntity) {
        return AccountRs.builder()
            .id(String.valueOf(accountEntity.getId()))
            .account(accountEntity.getNumber())
            .sum(accountEntity.getSum())
            .status(AccountRs.StatusEnum.valueOf(accountEntity.getStatus().toString()))
            .build();
    }

    public AccountUpdateRq mapToAccUpdateRq(final AccountEntity accountEntity) {
        return AccountUpdateRq.builder()
            .account(accountEntity.getNumber())
            .sum(accountEntity.getSum())
            .build();
    }
}
