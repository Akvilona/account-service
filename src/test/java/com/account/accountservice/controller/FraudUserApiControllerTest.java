package com.account.accountservice.controller;

import com.account.accountservice.support.IntegrationTestBase;
import com.openapi.accountservice.server.model.api.FraudUser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FraudUserApiControllerTest extends IntegrationTestBase {

    @Test
    void postFraudUserShouldBeWork() {
        FraudUser fraudUserRq = FraudUser.builder()
            .id(1L)
            .name("name")
            .email("email@email.com")
            .build();

        FraudUser fraudUserRs = postFraudUser( fraudUserRq, 200);

        assertThat(fraudUserRs)
            .usingRecursiveComparison()
            .isEqualTo(FraudUser.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build());
    }

    @Test
    void deleteFraudUserByEmail() {
    }

    private FraudUser postFraudUser(final FraudUser fraudUser, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "fraudUsers")
                .build())
            .bodyValue(fraudUser)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(FraudUser.class)
            .returnResult()
            .getResponseBody();
    }
}
