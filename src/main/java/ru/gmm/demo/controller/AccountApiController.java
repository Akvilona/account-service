/**
 * Создал Андрей Антонов 10/31/2023 5:35 PM.
 **/

package ru.gmm.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.gmm.demo.controller.api.AccApi;
import ru.gmm.demo.mapper.AccountMapper;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.api.AccRegistrationRq;
import ru.gmm.demo.model.api.AccRegistrationRs;
import ru.gmm.demo.model.api.AccRs;
import ru.gmm.demo.model.api.AccUpdateRq;
import ru.gmm.demo.service.AccountApiService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountApiController implements AccApi {
    private final AccountApiService accountApiService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<AccRegistrationRs> createAcc(final AccRegistrationRq accRegistrationRq) {
        final AccountEntity accountEntity = accountMapper.accUpdateRq(accRegistrationRq);
        accountApiService.createAcc(accountEntity);
        final AccRegistrationRs accRegistrationRs = accountMapper.mapToAccRegistrationRs(accountEntity);
        return ResponseEntity.ok(accRegistrationRs);
    }

    @Override
    public ResponseEntity<List<AccRs>> getAllAcc() {
        final List<AccRs> accRsList = accountApiService.getAll().stream()
            .map(accountMapper::mapToAccRs)
            .toList();
        return ResponseEntity.ok(accRsList);
    }

    @Override
    public ResponseEntity<AccRs> getAccById(final String id) {
        final AccountEntity accountEntity = accountApiService.findById(id);
        final AccRs accRs = accountMapper.mapToAccRs(accountEntity);
        return ResponseEntity.ok(accRs);
    }

    @Override
    public ResponseEntity<AccUpdateRq> updateAcc(final String id, final AccUpdateRq accUpdateRq) {
        final AccountEntity accountEntity = accountApiService.updateAcc(id, accUpdateRq);
        final AccUpdateRq updateRq = accountMapper.mapToAccUpdateRq(accountEntity);
        return ResponseEntity.ok(updateRq);
    }

    @Override
    public ResponseEntity<Void> deleteAccById(final String id) {
        accountApiService.deleteAccById(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }
}

