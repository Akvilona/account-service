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
import ru.gmm.demo.model.api.TransactionRs;
import ru.gmm.demo.model.api.TransactionUpdateRq;
import ru.gmm.demo.repository.AccountRepository;
import ru.gmm.demo.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ru.gmm.demo.exception.ErrorCode.ERR_CODE_003;
import static ru.gmm.demo.exception.ErrorCode.ERR_CODE_008;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionApiService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionEntity createTransaction(final CreateTransactionRq createTransactionRq) {
        return switch (createTransactionRq.getType()) {
            case TRANSFER -> createTransferTransaction(createTransactionRq);
            case DEPOSIT -> createDepositTransaction(createTransactionRq);
            case WITHDRAWAL -> createWithdrawalTransaction(createTransactionRq);
        };
    }

    private TransactionEntity createDepositTransaction(final CreateTransactionRq request) {
        final AccountEntity accountTo = accountRepository.findOpenedAccountByNumber(request.getAccountTo())
            .orElseThrow(() -> new ServiceException(ERR_CODE_003, request.getAccountTo()));

        final BigDecimal transactionSum = request.getSum();
        accountTo.setSum(accountTo.getSum().add(transactionSum));

        final TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(request, null, accountTo);
        return transactionRepository.save(transactionEntity);
    }

    private TransactionEntity createWithdrawalTransaction(final CreateTransactionRq request) {
        final AccountEntity accountFrom = accountRepository.findOpenedAccountByNumber(request.getAccountFrom())
            .orElseThrow(() -> new ServiceException(ERR_CODE_003, request.getAccountTo()));

        final BigDecimal transactionSum = request.getSum();
        final BigDecimal remainingBalance = accountFrom.getSum().subtract(transactionSum);
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException(ErrorCode.ERR_CODE_004, accountFrom.getId());
        }

        accountFrom.setSum(remainingBalance);

        final TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(request, null, accountFrom);
        return transactionRepository.save(transactionEntity);
    }

    private TransactionEntity createTransferTransaction(final CreateTransactionRq request) {
        if (request.getAccountFrom().equals(request.getAccountTo())) {
            throw new ServiceException(ERR_CODE_008, request.getAccountFrom());
        }

        final AccountEntity accountFrom = accountRepository.findOpenedAccountByNumber(request.getAccountFrom())
            .orElseThrow(() -> new ServiceException(ERR_CODE_003, request.getAccountFrom()));

        final BigDecimal transactionSum = request.getSum();
        final BigDecimal remainingBalance = accountFrom.getSum().subtract(transactionSum);
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException(ErrorCode.ERR_CODE_004, accountFrom.getId());
        }

        final AccountEntity accountTo = accountRepository.findOpenedAccountByNumber(request.getAccountTo())
            .orElseThrow(() -> new ServiceException(ERR_CODE_003, request.getAccountTo()));

        final BigDecimal newBalance = accountTo.getSum().add(transactionSum);
        accountFrom.setSum(remainingBalance);
        accountTo.setSum(newBalance);

        final TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(request, accountFrom, accountTo);
        return transactionRepository.save(transactionEntity);
    }

    public List<TransactionEntity> getAll() {
        return transactionRepository.fetchTransactionEntityList();
    }

    @Transactional(readOnly = true)
    public TransactionRs findById(final String id) {
        Optional<TransactionEntity> transactionOptional = transactionRepository.findById(Long.valueOf(id));
        transactionOptional.orElseThrow(() -> new ServiceException(ERR_CODE_003, id));
        return transactionMapper.toTransactionRs(transactionOptional.get());
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
