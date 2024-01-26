/**
 * Создал Андрей Антонов 11/12/2023 3:43 PM.
 **/

package com.account.accountservice.controller;

import com.account.accountservice.controller.api.TransactionApi;
import com.account.accountservice.mapper.TransactionMapper;
import com.account.accountservice.model.TransactionEntity;
import com.account.accountservice.model.api.CreateTransactionRq;
import com.account.accountservice.model.api.CreateTransactionRs;
import com.account.accountservice.model.api.TransactionRs;
import com.account.accountservice.model.api.TransactionUpdateRq;
import com.account.accountservice.service.TransactionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionApiController implements TransactionApi {
    private final TransactionApiService transactionApiService;
    private final TransactionMapper transactionMapper;

    @Override
    public ResponseEntity<CreateTransactionRs> createTransaction(final CreateTransactionRq request) {
        final TransactionEntity transactionEntity = transactionApiService.createTransaction(request);
        final CreateTransactionRs transactionRegistrationRs = transactionMapper.toCreateTransactionRs(transactionEntity);
        return ResponseEntity.ok(transactionRegistrationRs);
    }

    @Override
    public ResponseEntity<List<TransactionRs>> getAllTransaction() {
        final List<TransactionRs> transactionRs = transactionApiService.getAll()
            .stream()
            .map(transactionMapper::toTransactionRs)
            .toList();
        return ResponseEntity.ok(transactionRs);
    }

    @Override
    public ResponseEntity<TransactionRs> getTransactionById(final String id) {
        return ResponseEntity.ok(transactionApiService.findById(id));
    }

    @Override
    public ResponseEntity<TransactionUpdateRq> updateTransaction(final String id, final TransactionUpdateRq transactionUpdateRq) {
        final TransactionEntity transactionEntity = transactionApiService.updateTransaction(id, transactionUpdateRq);
        final TransactionUpdateRq updateRq = transactionMapper.toTransactionUpdateRq(transactionEntity);
        return ResponseEntity.ok(updateRq);
    }

    @Override
    public ResponseEntity<Void> deleteTransactionById(final String id) {
        transactionApiService.deleteTransactionById(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }
}
