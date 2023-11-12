/**
 * Создал Андрей Антонов 11/12/2023 3:43 PM.
 **/

package ru.gmm.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.gmm.demo.controller.api.TransactionApi;
import ru.gmm.demo.mapper.TransactionMapper;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.api.TransactionRegistrationRq;
import ru.gmm.demo.model.api.TransactionRegistrationRs;
import ru.gmm.demo.model.api.TransactionRs;
import ru.gmm.demo.model.api.TransactionUpdateRq;
import ru.gmm.demo.service.TransactionApiService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {
    private final TransactionApiService transactionApiService;
    private final TransactionMapper transactionMapper;

    @Override
    public ResponseEntity<TransactionRegistrationRs> createTransaction(final TransactionRegistrationRq transactionRegistrationRq) {
        final TransactionEntity transactionEntity = transactionMapper.transactionUpdateRq(transactionRegistrationRq);
        transactionApiService.createTransaction(transactionEntity);
        final TransactionRegistrationRs transactionRegistrationRs = transactionMapper.mapToTransactionRegistrationRs(transactionEntity);
        return ResponseEntity.ok(transactionRegistrationRs);
    }

    @Override
    public ResponseEntity<List<TransactionRs>> getAllTransaction() {
        final List<TransactionRs> transactionRs = transactionApiService.getAll().stream().map(transactionMapper::mapToTransactionRs).toList();
        return ResponseEntity.ok(transactionRs);
    }

    @Override
    public ResponseEntity<TransactionRs> getTransactionById(final String id) {
        final TransactionEntity transactionEntity = transactionApiService.findById(id);
        final TransactionRs transactionRs = transactionMapper.mapToTransactionRs(transactionEntity);
        return ResponseEntity.ok(transactionRs);
    }

    @Override
    public ResponseEntity<TransactionUpdateRq> updateTransaction(final String id, final TransactionUpdateRq transactionUpdateRq) {
        final TransactionEntity transactionEntity = transactionApiService.updateTransaction(id, transactionUpdateRq);
        final TransactionUpdateRq updateRq = transactionMapper.mapToTransactionUpdateRq(transactionEntity);
        return ResponseEntity.ok(updateRq);
    }

    @Override
    public ResponseEntity<Void> deleteTransactionById(final String id) {
        transactionApiService.deleteTransactionById(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }
}
