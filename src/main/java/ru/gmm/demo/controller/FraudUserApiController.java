/**
 * Создал Андрей Антонов 12/14/2023 12:53 PM.
 **/

package ru.gmm.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.gmm.demo.controller.api.FraudUserApi;
import ru.gmm.demo.model.api.FraudUser;
import ru.gmm.demo.service.FraudUserService;

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
        return FraudUserApi.super.deleteFraudUserByEmail(id);
    }
}
