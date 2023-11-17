/**
 * Создал Андрей Антонов 11/1/2023 9:03 AM.
 **/

package ru.gmm.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.AccountRegistrationRq;
import ru.gmm.demo.model.api.AccountRegistrationRs;
import ru.gmm.demo.model.api.AccountRs;
import ru.gmm.demo.model.api.AccountUpdateRq;
import ru.gmm.demo.model.api.TransactionRs;

import java.util.List;
import java.util.Random;

@Mapper(config = MapperConfiguration.class)
public interface AccountMapper {
    Random RANDOM = new Random();

    default AccountEntity toAccountEntity(final AccountRegistrationRq accRegistrationRq) {
        return AccountEntity.builder()
            .id(RANDOM.nextLong())
            .number(accRegistrationRq.getAccount())
            .sum(accRegistrationRq.getSum())
            .build();
    }

    default AccountEntity toAccountEntity(final AccountEntity accountEntity, final AccountUpdateRq accountUpdateRq) {
        accountEntity.setNumber(accountUpdateRq.getAccount());
        accountEntity.setSum(accountUpdateRq.getSum());
        return accountEntity;
    }

    default AccountEntity toAccountEntityAndUserEntity(final AccountEntity accountEntity, final UserEntity userEntity) {
        return AccountEntity.builder()
            .id(RANDOM.nextLong())
            .user(userEntity)
            .number(accountEntity.getNumber())
            .status(accountEntity.getStatus())
            .sum(accountEntity.getSum())
            .build();
    }

    default AccountRegistrationRs toAccountRegistrationRs(final AccountEntity accountEntity) {
        return AccountRegistrationRs.builder()
            .id(String.valueOf(accountEntity.getId()))
            .sum(accountEntity.getSum())
            .build();
    }

    @Named("mapToAccRs")
    default AccountRs mapToAccRs(final AccountEntity accountEntity) {
        List<TransactionRs> transactionFromRs = accountEntity.getAccountsFrom().stream()
            .map(this::mapToTransactionRs)
            .toList();
        List<TransactionRs> transactionToRs = accountEntity.getAccountsTo().stream()
            .map(this::mapToTransactionRs)
            .toList();

        return AccountRs.builder()
            .id(String.valueOf(accountEntity.getId()))
            .account(accountEntity.getNumber())
            .sum(accountEntity.getSum())
            .transactionsFrom(transactionFromRs)
            .transactionsTo(transactionToRs)
            .status(AccountRs.StatusEnum.valueOf(accountEntity.getStatus().toString()))
            .build();
    }

    private TransactionRs mapToTransactionRs(final TransactionEntity transaction) {
        return TransactionRs.builder()
            .id(String.valueOf(transaction.getId()))
            .status(String.valueOf(transaction.getType()))
            .sum(transaction.getSum())
            .description(transaction.getDescription())
            .createDateTime(transaction.getAudit().getCreateDateTime().toString())
            .build();
    }

    default AccountUpdateRq mapToAccUpdateRq(final AccountEntity accountEntity) {
        return AccountUpdateRq.builder()
            .account(accountEntity.getNumber())
            .sum(accountEntity.getSum())
            .build();
    }
}
