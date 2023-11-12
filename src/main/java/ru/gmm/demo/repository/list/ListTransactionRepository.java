/**
* Создал Андрей Антонов 11/12/2023 12:58 PM.
**/

package ru.gmm.demo.repository.list;

import org.springframework.stereotype.Component;
import ru.gmm.demo.exception.ErrorCode;
import ru.gmm.demo.exception.ServiceException;
import ru.gmm.demo.model.TransactionEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListTransactionRepository {
    private final List<TransactionEntity> transactionEntityList = new ArrayList<>();

    public void save(final TransactionEntity transactionEntity) {
        transactionEntityList.add(transactionEntity);
    }

    public TransactionEntity get(final Long id) {
        return findById(id);
    }

    public List<TransactionEntity> getAll() {
        return transactionEntityList;
    }

    public boolean deleteTransactionById(final Long id) {
        return transactionEntityList.removeIf(transactionEntity -> transactionEntity.getId().equals(id));
    }

    private TransactionEntity findById(final Long id) {
        return transactionEntityList.stream()
            .filter(transactionEntity -> transactionEntity.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, id));
    }
}
