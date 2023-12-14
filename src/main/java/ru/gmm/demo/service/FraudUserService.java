/**
 * Создал Андрей Антонов 12/14/2023 1:23 PM.
 **/

package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gmm.demo.client.FraudClient;
import ru.gmm.demo.model.api.FraudUser;

@RequiredArgsConstructor
@Service
@Slf4j
public class FraudUserService {
    private final FraudClient fraudClient;

    public FraudUser createFraudUserInFraudService(final FraudUser fraudUser) {
        return fraudClient.postFraudUserById(fraudUser);
    }

    public void deleteFraudUserByIdInInFraudService(final Long id) {
        fraudClient.deleteFraudUserById(id);
    }
}
