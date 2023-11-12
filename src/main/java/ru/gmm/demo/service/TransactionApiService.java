/**
 * Создал Андрей Антонов 11/12/2023 3:08 PM.
 **/

package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gmm.demo.mapper.TransactionMapper;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.api.TransactionUpdateRq;
import ru.gmm.demo.repository.id.TransactionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionApiService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public void createTransaction(final TransactionEntity transactionEntity) {
        transactionRepository.save(transactionEntity);
    }

    public List<TransactionEntity> getAll() {
        return transactionRepository.findAll();
    }

    public TransactionEntity findById(final String id) {
        return transactionRepository.findById(Long.valueOf(id)).orElseThrow();
    }

    public TransactionEntity updateTransaction(final String id, final TransactionUpdateRq transactionUpdateRq) {
        final TransactionEntity transactionEntity = transactionRepository.findById(Long.parseLong(id))
            .orElseThrow();

        return transactionMapper.transactionUpdateRq(transactionEntity, transactionUpdateRq);
    }

    public void deleteTransactionById(final Long id) {
        transactionRepository.deleteById(id);
    }
}
