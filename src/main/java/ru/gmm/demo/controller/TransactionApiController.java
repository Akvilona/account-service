/**
 * Создал Андрей Антонов 11/12/2023 3:43 PM.
 **/

package ru.gmm.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.gmm.demo.controller.api.TransactionApi;
import ru.gmm.demo.mapper.TransactionMapper;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.api.CreateTransactionRq;
import ru.gmm.demo.model.api.CreateTransactionRs;
import ru.gmm.demo.model.api.TransactionRs;
import ru.gmm.demo.model.api.TransactionUpdateRq;
import ru.gmm.demo.service.TransactionApiService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionApiController implements TransactionApi {
    private final TransactionApiService transactionApiService;
    private final TransactionMapper transactionMapper;

    public ResponseEntity<CreateTransactionRs> createTransaction(final CreateTransactionRq request) {
        try {
            final TransactionEntity transactionEntity = transactionApiService.createTransaction(request);
            final CreateTransactionRs transactionRegistrationRs = transactionMapper.toCreateTransactionRs(transactionEntity);
            return ResponseEntity.ok(transactionRegistrationRs);
        } catch (Exception e) {
            e.printStackTrace(); // Выводим стек трейс для отладки
            CreateTransactionRs errorResponse = new CreateTransactionRs();
            errorResponse.setId(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
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
        final TransactionEntity transactionEntity = transactionApiService.findById(id);
        final TransactionRs transactionRs = transactionMapper.toTransactionRs(transactionEntity);
        return ResponseEntity.ok(transactionRs);
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
