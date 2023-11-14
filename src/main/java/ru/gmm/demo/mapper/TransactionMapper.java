/**
 * Создал Андрей Антонов 11/12/2023 1:37 PM.
 **/

package ru.gmm.demo.mapper;

import org.springframework.stereotype.Component;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.api.CreateTransactionRq;
import ru.gmm.demo.model.api.CreateTransactionRs;
import ru.gmm.demo.model.api.TransactionRs;
import ru.gmm.demo.model.api.TransactionUpdateRq;
import ru.gmm.demo.model.enums.TransactionStatus;

import java.util.Random;

@Component
public class TransactionMapper {
    public static final Random RANDOM = new Random();

    public TransactionEntity toTransactionEntity(final CreateTransactionRq createTransactionRq,
                                                 final AccountEntity accountFrom,
                                                 final AccountEntity accountTo) {
        return TransactionEntity.builder()
            .id(RANDOM.nextLong())
            .sum(createTransactionRq.getSum())
            .status(TransactionStatus.valueOf(createTransactionRq.getStatus().toString()))
            .accountFrom(accountFrom)
            .accountTo(accountTo)
            .description(createTransactionRq.getDescription())
            .build();
    }

    public TransactionEntity toTransactionEntity(final TransactionEntity transactionEntity, final TransactionUpdateRq transactionUpdateRq) {
        transactionEntity.setSum(transactionUpdateRq.getSum());
        transactionEntity.setStatus(TransactionStatus.valueOf(transactionUpdateRq.getStatus()));
        transactionEntity.setDescription(transactionUpdateRq.getDescription());
        return transactionEntity;
    }

    public CreateTransactionRs mapToTransactionRegistrationRs(final TransactionEntity transactionEntity) {
        return CreateTransactionRs.builder()
            .id(String.valueOf(transactionEntity.getId()))
            .sum(transactionEntity.getSum())
            .status(String.valueOf(transactionEntity.getStatus()))
            .description(transactionEntity.getDescription())
            .build();
    }

    public TransactionRs toTransactionRs(final TransactionEntity transactionEntity) {
        return TransactionRs.builder()
            .id(String.valueOf(transactionEntity.getId()))
            .accountFrom(transactionEntity.getAccountFrom() != null
                        ? transactionEntity.getAccountFrom().getNumber()
                        : null)
            .accountTo(transactionEntity.getAccountTo() != null
                       ? transactionEntity.getAccountTo().getNumber()
                       : null)
            .sum(transactionEntity.getSum())
            .status(transactionEntity.getAccountFrom() != null || transactionEntity.getAccountTo() != null
                    ? String.valueOf(TransactionStatus.WITHDRAWAL)
                    : String.valueOf(transactionEntity.getStatus()))
            .description(transactionEntity.getDescription())
            .createDateTime(transactionEntity.getAudit().getCreateDateTime().toString())
            .updateDateTime(transactionEntity.getAudit().getUpdateDateTime().toString())
            .build();
    }

    public TransactionUpdateRq mapToTransactionUpdateRq(final TransactionEntity transactionEntity) {
        return TransactionUpdateRq.builder()
            .id(String.valueOf(transactionEntity.getId()))
            .sum(transactionEntity.getSum())
            .status(String.valueOf(transactionEntity.getStatus()))
            .description(transactionEntity.getDescription())
            .build();
    }
}
