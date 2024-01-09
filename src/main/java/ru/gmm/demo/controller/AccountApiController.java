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
//@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class AccountApiController implements AccountApi {
    private final AccountApiService accountApiService;
    private final AccountMapper accountMapper;
    //    @Lazy
    //    private final AccountApiController self;

    @Override
    public ResponseEntity<AccountRegistrationRs> createAccount(final AccountRegistrationRq request) {
        final AccountEntity account = accountApiService.createAccount(request);
        final AccountRegistrationRs accRegistrationRs = accountMapper.toAccountRegistrationRs(account);
        return ResponseEntity.ok(accRegistrationRs);
    }

    @Override
    public ResponseEntity<List<AccountRs>> getAllAccount() {
        return ResponseEntity.ok(accountApiService.getAll());
    }

    /*  @Transactional(readOnly = true)
    public ResponseEntity<List<AccountRs>> getAccountList() {
        final List<AccountRs> accRsList = accountApiService.getAll().stream()
            .map(accountMapper::toAccountRs)
            .toList();
        return ResponseEntity.ok(accRsList);
    }*/

    @Override
    public ResponseEntity<AccountRs> getAccountById(final String id) {
        return ResponseEntity.ok(accountApiService.findById(id));
    }

    @Override
    public ResponseEntity<AccountUpdateRq> updateAccount(final String id, final AccountUpdateRq request) {
        final AccountEntity accountEntity = accountApiService.updateAccount(id, request);
        return ResponseEntity.ok(accountMapper.toAccountUpdateRq(accountEntity));
    }

    @Override
    public ResponseEntity<Void> deleteAccountById(final String id) {
        accountApiService.deleteAccById(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }
}

