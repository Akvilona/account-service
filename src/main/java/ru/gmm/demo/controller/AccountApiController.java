/**
 * Создал Андрей Антонов 10/31/2023 5:35 PM.
 **/

package ru.gmm.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.gmm.demo.controller.api.AccountApi;
import ru.gmm.demo.mapper.AccountMapper;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.api.AccountRegistrationRq;
import ru.gmm.demo.model.api.AccountRegistrationRs;
import ru.gmm.demo.model.api.AccountRs;
import ru.gmm.demo.model.api.AccountUpdateRq;
import ru.gmm.demo.service.AccountApiService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountApiController implements AccountApi {
    private final AccountApiService accountApiService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<AccountRegistrationRs> createAccount(final AccountRegistrationRq accRegistrationRq) {
        final AccountEntity accountEntity = accountMapper.toAccountEntity(accRegistrationRq);
        accountApiService.createAccount(accountEntity);
        final AccountRegistrationRs accRegistrationRs = accountMapper.toAccountRegistrationRs(accountEntity);
        return ResponseEntity.ok(accRegistrationRs);
    }

    @Override
    public ResponseEntity<List<AccountRs>> getAllAccount() {
        final List<AccountRs> accRsList = accountApiService.getAll().stream()
            .map(accountMapper::mapToAccRs)
            .toList();
        return ResponseEntity.ok(accRsList);
    }

    @Override
    public ResponseEntity<AccountRs> getAccountById(final String id) {
        final AccountEntity accountEntity = accountApiService.findById(id);
        final AccountRs accRs = accountMapper.mapToAccRs(accountEntity);
        return ResponseEntity.ok(accRs);
    }

    @Override
    public ResponseEntity<AccountUpdateRq> updateAccount(final String id, final AccountUpdateRq accUpdateRq) {
        final AccountEntity accountEntity = accountApiService.updateAccount(id, accUpdateRq);
        final AccountUpdateRq updateRq = accountMapper.mapToAccUpdateRq(accountEntity);
        return ResponseEntity.ok(updateRq);
    }

    @Override
    public ResponseEntity<Void> deleteAccountById(final String id) {
        accountApiService.deleteAccById(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }
}

