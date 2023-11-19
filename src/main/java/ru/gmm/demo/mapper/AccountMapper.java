package ru.gmm.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
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

@Component
@Mapper(config = MapperConfiguration.class)
public interface AccountMapper {
    Random RANDOM = new Random();


    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "number", source = "accRegistrationRq.account"),
        @Mapping(target = "sum", source = "accRegistrationRq.sum")
    })
    AccountEntity toAccountEntity(AccountRegistrationRq accRegistrationRq);

    @Mappings({
        @Mapping(target = "number", source = "accountUpdateRq.account"),
        @Mapping(target = "sum", source = "accountUpdateRq.sum")
    })
    AccountEntity toAccountEntity(AccountEntity accountEntity, AccountUpdateRq accountUpdateRq);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "user", source = "userEntity"),
        @Mapping(target = "number", source = "accountEntity.number"),
        @Mapping(target = "status", source = "accountEntity.status"),
        @Mapping(target = "sum", source = "accountEntity.sum"),
        @Mapping(target = "audit.createDateTime", source = "userEntity.audit.createDateTime"),
        @Mapping(target = "audit.updateDateTime", source = "userEntity.audit.updateDateTime")
    })
    AccountEntity toAccountEntityAndUserEntity(AccountEntity accountEntity, UserEntity userEntity);

    @Mappings({
        @Mapping(target = "id", source = "accountEntity.id"),
        @Mapping(target = "sum", source = "accountEntity.sum")
    })
    AccountRegistrationRs toAccountRegistrationRs(AccountEntity accountEntity);

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

    @Mappings({
        @Mapping(target = "account", source = "accountEntity.number"),
        @Mapping(target = "sum", source = "accountEntity.sum")
    })
    AccountUpdateRq mapToAccUpdateRq(AccountEntity accountEntity);
}
