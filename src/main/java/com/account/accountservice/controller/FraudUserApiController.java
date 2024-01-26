/**
 * Создал Андрей Антонов 12/14/2023 12:53 PM.
 **/

package com.account.accountservice.controller;

import com.account.accountservice.controller.api.FraudUserApi;
import com.account.accountservice.model.api.FraudUser;
import com.account.accountservice.service.FraudUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FraudUserApiController implements FraudUserApi {
    private final FraudUserService fraudUserService;

    @Override
    public ResponseEntity<FraudUser> postFraudUserById(final FraudUser fraudUser) {
        return ResponseEntity.ok(fraudUserService.createFraudUserInFraudService(fraudUser));
    }

    @Override
    public ResponseEntity<Void> deleteFraudUserByEmail(final String id) {
        fraudUserService.deleteFraudUserByIdInInFraudService(Long.valueOf(id));
        return ResponseEntity.ok(null);
    }
}
