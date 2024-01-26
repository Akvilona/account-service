package com.account.accountservice.support;

import com.account.accountservice.model.AccountEntity;
import com.account.accountservice.model.TransactionEntity;
import com.account.accountservice.model.UserEntity;
import com.account.accountservice.model.enums.AccountStatus;
import com.account.accountservice.model.enums.TransactionType;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class DataProvider {
    public static UserEntity getUserEntity() {
        TransactionEntity transactionEntity1 = TransactionEntity.builder()
            .sum(new BigDecimal("2000.0"))
            .type(TransactionType.DEPOSIT)
            .build();
        TransactionEntity transactionEntity2 = TransactionEntity.builder()
            .sum(new BigDecimal("2000.0"))
            .type(TransactionType.DEPOSIT)
            .build();
        TransactionEntity transactionEntity3 = TransactionEntity.builder()
            .sum(new BigDecimal("3000.0"))
            .type(TransactionType.TRANSFER)
            .build();
        TransactionEntity transactionEntity4 = TransactionEntity.builder()
            .sum(new BigDecimal("1000.0"))
            .type(TransactionType.WITHDRAWAL)
            .build();
        AccountEntity account1 = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("0123456")
            .build()
            .withTransactionTo(transactionEntity1)
            .withTransactionTo(transactionEntity2)
            .withTransactionsFrom(transactionEntity3);
        AccountEntity account2 = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("1234567")
            .build()
            .withTransactionTo(transactionEntity3)
            .withTransactionsFrom(transactionEntity4);

        return UserEntity.builder()
            .name("test")
            .password("pass")
            .build()
            .withAccount(account1)
            .withAccount(account2);
    }

    public static UserEntity getUserEntity(final String name,
                                           final String password,
                                           final String accountNumber) {
        TransactionEntity transactionEntity = TransactionEntity.builder()
            .sum(new BigDecimal("2000.0"))
            .type(TransactionType.DEPOSIT)
            .build();
        AccountEntity account1 = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number(accountNumber + "_1")
            .build();
        AccountEntity account2 = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number(accountNumber + "_2")
            .build();

        account1.withTransactionTo(transactionEntity);
        account2.withTransactionsFrom(transactionEntity);

        return UserEntity.builder()
            .name(name)
            .password(password)
            .build()
            .withAccount(account1)
            .withAccount(account2);
    }
}
