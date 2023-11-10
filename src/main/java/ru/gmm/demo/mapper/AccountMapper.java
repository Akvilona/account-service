/**
 * Создал Андрей Антонов 11/1/2023 9:03 AM.
 **/

package ru.gmm.demo.mapper;

import org.springframework.stereotype.Component;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.api.AccRegistrationRq;
import ru.gmm.demo.model.api.AccRegistrationRs;
import ru.gmm.demo.model.api.AccRs;
import ru.gmm.demo.model.api.AccUpdateRq;

import java.util.Random;

@Component
public class AccountMapper {
    public static final Random RANDOM = new Random();

    public AccountEntity accUpdateRq(final AccRegistrationRq accRegistrationRq) {
        return AccountEntity.builder()
            .id(RANDOM.nextLong())
            .account(accRegistrationRq.getAccount())
            .sum(accRegistrationRq.getSum())
            .build();
    }

    public AccountEntity accUpdateRq(final AccountEntity accountEntity, final AccUpdateRq accUpdateRq) {
        accountEntity.setAccount(accUpdateRq.getAccount());
        accountEntity.setSum(accUpdateRq.getSum());
        return accountEntity;
    }

    public AccRegistrationRs mapToAccRegistrationRs(final AccountEntity accountEntity) {
        return AccRegistrationRs.builder()
            .id(String.valueOf(accountEntity.getId()))
            .sum(accountEntity.getSum())
            .build();
    }

    public AccRs mapToAccRs(final AccountEntity accountEntity) {
        return AccRs.builder()
            .id(String.valueOf(accountEntity.getId()))
            .account(accountEntity.getAccount())
            .sum(accountEntity.getSum())
            .build();
    }

    public AccUpdateRq mapToAccUpdateRq(final AccountEntity accountEntity) {
        return AccUpdateRq.builder()
            .account(accountEntity.getAccount())
            .sum(accountEntity.getSum())
            .build();
    }
}
