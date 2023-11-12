/**
 * Создал Андрей Антонов 11/12/2023 1:37 PM.
 **/

package ru.gmm.demo.mapper;

import org.springframework.stereotype.Component;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.api.TransactionRegistrationRq;
import ru.gmm.demo.model.api.TransactionRegistrationRs;
import ru.gmm.demo.model.api.TransactionRs;
import ru.gmm.demo.model.api.TransactionUpdateRq;
import ru.gmm.demo.model.enums.TransactionStatus;

import java.util.Random;

@Component
public class TransactionMapper {
    public static final Random RANDOM = new Random();

    public TransactionEntity transactionUpdateRq(final TransactionRegistrationRq transactionRegistrationRq) {
        return TransactionEntity.builder()
            .id(RANDOM.nextLong())
            .accountFrom(transactionRegistrationRq.getAccountFrom())
            .accountTo(transactionRegistrationRq.getAccountTo())
            .sum(transactionRegistrationRq.getSum())
            .status(TransactionStatus.valueOf(transactionRegistrationRq.getStatus()))
            .description(transactionRegistrationRq.getDescription())
            .build();
    }

    public TransactionEntity transactionUpdateRq(final TransactionEntity transactionEntity, final TransactionUpdateRq transactionUpdateRq) {
        transactionEntity.setAccountFrom(transactionUpdateRq.getAccountFrom());
        transactionEntity.setAccountTo(transactionUpdateRq.getAccountTo());
        transactionEntity.setSum(transactionUpdateRq.getSum());
        transactionEntity.setStatus(TransactionStatus.valueOf(transactionUpdateRq.getStatus()));
        transactionEntity.setDescription(transactionUpdateRq.getDescription());
        return transactionEntity;
    }

    public TransactionRegistrationRs mapToTransactionRegistrationRs(final TransactionEntity transactionEntity) {
        return TransactionRegistrationRs.builder()
            .id(String.valueOf(transactionEntity.getId()))
            .accountFrom(transactionEntity.getAccountFrom())
            .accountTo(transactionEntity.getAccountTo())
            .sum(transactionEntity.getSum())
            .status(String.valueOf(transactionEntity.getStatus()))
            .description(transactionEntity.getDescription())
            .build();
    }

    public TransactionRs mapToTransactionRs(final TransactionEntity transactionEntity) {
        return TransactionRs.builder()
            .id(String.valueOf(transactionEntity.getId()))
            .accountFrom(transactionEntity.getAccountFrom())
            .accountTo(transactionEntity.getAccountTo())
            .sum(transactionEntity.getSum())
            .status(String.valueOf(transactionEntity.getStatus()))
            .description(transactionEntity.getDescription())
            .build();
    }

    public TransactionUpdateRq mapToTransactionUpdateRq(final TransactionEntity transactionEntity) {
        return TransactionUpdateRq.builder()
            .id(String.valueOf(transactionEntity.getId()))
            .accountFrom(transactionEntity.getAccountFrom())
            .accountTo(transactionEntity.getAccountTo())
            .sum(transactionEntity.getSum())
            .status(String.valueOf(transactionEntity.getStatus()))
            .description(transactionEntity.getDescription())
            .build();
    }
}
