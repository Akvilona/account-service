/**
 * Создал Андрей Антонов 12/14/2023 12:53 PM.
 **/

package com.account.accountservice.controller;

import com.account.accountservice.service.FraudUserService;
import com.openapi.accountservice.server.controller.api.FraudUserApi;
import com.openapi.accountservice.server.model.api.FraudUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FraudUserApiController implements FraudUserApi {
    private final FraudUserService fraudUserService;

    @Override
    public ResponseEntity<FraudUser> postFraudUser(final FraudUser fraudUser) {
        return ResponseEntity.ok(fraudUserService.createFraudUserInFraudService(fraudUser));
    }

    @Override
    public ResponseEntity<Void> deleteFraudUserByEmail(final String id) {
        fraudUserService.deleteFraudUserByIdInInFraudService(Long.valueOf(id));
        return ResponseEntity.ok(null);
    }
}
