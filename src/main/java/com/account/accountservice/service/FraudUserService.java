/**
 * Создал Андрей Антонов 12/14/2023 1:23 PM.
 **/

package com.account.accountservice.service;

import com.account.accountservice.exception.ErrorCode;
import com.account.accountservice.exception.ServiceException;
import com.openapi.accountservice.server.model.api.FraudUser;
import com.openapi.fraudservice.client.api.FraudUserApi;
import com.openapi.fraudservice.client.dto.FraudUserFraudRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class FraudUserService {
    private final FraudUserApi fraudClient;

    public FraudUser createFraudUserInFraudService(final FraudUser fraudUser) {
        FraudUserFraudRequest fraudUserRequest = new FraudUserFraudRequest();
        fraudUserRequest.setId(fraudUser.getId());
        fraudUserRequest.setUserEmail(fraudUser.getEmail());
        fraudUserRequest.setFirstName(fraudUser.getName());
        fraudUserRequest.setAge(18);

        FraudUserFraudRequest result = fraudClient.postFraudUserById(fraudUserRequest)
            .block();

        if (result == null) {
            throw new ServiceException(ErrorCode.ERR_CODE_009);
        }

        return FraudUser.builder()
            .id(result.getId())
            .email(result.getUserEmail())
            .name(result.getFirstName())
            .build();
    }

    public void deleteFraudUserByIdInInFraudService(final Long id) {
        fraudClient.deleteFraudUserById(id);
    }
}
