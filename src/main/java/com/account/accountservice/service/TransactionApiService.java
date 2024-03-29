package com.account.accountservice.service;

import com.account.accountservice.exception.ErrorCode;
import com.account.accountservice.exception.ServiceException;
import com.account.accountservice.mapper.TransactionMapper;
import com.account.accountservice.model.AccountEntity;
import com.account.accountservice.model.TransactionEntity;
import com.account.accountservice.repository.AccountRepository;
import com.account.accountservice.repository.TransactionRepository;
import com.openapi.accountservice.server.model.api.CreateTransactionRq;
import com.openapi.accountservice.server.model.api.TransactionRs;
import com.openapi.accountservice.server.model.api.TransactionUpdateRq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_003, request.getAccountTo()));

        accountTo.setSum(accountTo.getSum().add(request.getSum()));

        final TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(request, null, accountTo);
        return transactionRepository.save(transactionEntity);
    }

    private TransactionEntity createWithdrawalTransaction(final CreateTransactionRq request) {
        final AccountEntity accountFrom = accountRepository.findOpenedAccountByNumber(request.getAccountFrom())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_003, request.getAccountTo()));

        subtractTransactionSumFromAccount(accountFrom, request.getSum());

        final TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(request, null, accountFrom);
        return transactionRepository.save(transactionEntity);
    }

    private TransactionEntity createTransferTransaction(final CreateTransactionRq request) {
        if (request.getAccountFrom().equals(request.getAccountTo())) {
            throw new ServiceException(ErrorCode.ERR_CODE_008, request.getAccountFrom());
        }

        final AccountEntity accountFrom = accountRepository.findOpenedAccountByNumber(request.getAccountFrom())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_003, request.getAccountFrom()));

        subtractTransactionSumFromAccount(accountFrom, request.getSum());

        final AccountEntity accountTo = accountRepository.findOpenedAccountByNumber(request.getAccountTo())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_003, request.getAccountTo()));

        accountTo.setSum(accountTo.getSum().add(request.getSum()));

        final TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(request, accountFrom, accountTo);
        return transactionRepository.save(transactionEntity);
    }

    private void subtractTransactionSumFromAccount(final AccountEntity account, final BigDecimal transactionSum) {
        BigDecimal remainingBalance = account.getSum().subtract(transactionSum);
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException(ErrorCode.ERR_CODE_004, account.getId());
        }
        account.setSum(remainingBalance);
    }

    public List<TransactionEntity> getAll() {
        return transactionRepository.fetchTransactionEntityList();
    }

    @Transactional(readOnly = true)
    public TransactionRs findById(final String id) {
        Optional<TransactionEntity> transactionOptional = transactionRepository.findById(Long.valueOf(id));
        transactionOptional.orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_003, id));
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
