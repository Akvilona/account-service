package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gmm.demo.exception.ErrorCode;
import ru.gmm.demo.exception.ServiceException;
import ru.gmm.demo.mapper.TransactionMapper;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.api.CreateTransactionRq;
import ru.gmm.demo.model.api.TransactionUpdateRq;
import ru.gmm.demo.model.enums.AccountStatus;
import ru.gmm.demo.repository.AccountRepository;
import ru.gmm.demo.repository.TransactionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionApiService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    public TransactionEntity createTransaction(final CreateTransactionRq createTransactionRq) {
        final AccountEntity accountFrom = accountRepository.findByNumberAndStatus(createTransactionRq.getAccountFrom(), AccountStatus.OPENED)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_003, createTransactionRq.getAccountFrom()));
        final AccountEntity accountTo = accountRepository.findByNumberAndStatus(createTransactionRq.getAccountTo(), AccountStatus.OPENED)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_003, createTransactionRq.getAccountTo()));

        final TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(createTransactionRq, accountFrom, accountTo);
        return transactionRepository.save(transactionEntity);
    }

    public List<TransactionEntity> getAll() {
        return transactionRepository.fetchTransactionEntityList();
    }

    public TransactionEntity findById(final String id) {
        return transactionRepository.findById(Long.valueOf(id)).orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionEntity updateTransaction(final String id, final TransactionUpdateRq transactionUpdateRq) {
        final TransactionEntity transactionEntity = transactionRepository.findById(Long.parseLong(id))
            .orElseThrow();

        return transactionMapper.toTransactionEntity(transactionEntity, transactionUpdateRq);
    }

    public void deleteTransactionById(final Long id) {
        transactionRepository.deleteById(id);
    }
}
