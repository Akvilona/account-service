package com.account.accountservice.controller;

import com.account.accountservice.support.IntegrationTestBase;
import com.openapi.accountservice.server.model.api.FraudUser;
import com.openapi.fraudservice.client.dto.FraudUserFraudRequest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FraudUserApiControllerTest extends IntegrationTestBase {

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void deleteFraudUserByEmail() {
        deleteFraudUserByEmail("1");
    }

    @Test
    void checkFraudUserByEmail() {
        when(fraudUserApi.checkFraudUserByEmail(any(String.class)))
            .thenAnswer(invocation -> Mono.just(Boolean.TRUE));

        Boolean checkFraudUserByEmail = getCheckFraudUserByEmail("mail@mai.com", 200);
        assertTrue(checkFraudUserByEmail);
    }

    @Test
    void postFraudUserShouldBeWork() {
        // Arrange
        FraudUser fraudUserRq = FraudUser.builder()
            .id(2L)
            .name("name5")
            .email("email5@email.com")
            .build();

        FraudUserFraudRequest fraudUserRequest = new FraudUserFraudRequest()
            .id(fraudUserRq.getId())
            .firstName(fraudUserRq.getName())
            .userEmail(fraudUserRq.getEmail())
            .age(25);

        when(fraudUserApi.postFraudUserById(any(FraudUserFraudRequest.class)))
            .thenAnswer(invocation -> Mono.just(fraudUserRequest));

        // Act
        FraudUser fraudUserRs = postFraudUser(fraudUserRq, 200);

        // Assert
        assertThat(fraudUserRs)
            .usingRecursiveComparison()
            .isEqualTo(FraudUser.builder()
                .id(fraudUserRq.getId())
                .name(fraudUserRq.getName())
                .email(fraudUserRq.getEmail())
                .build());
    }

    private Boolean getCheckFraudUserByEmail(final String email, final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "fraudUsers")
                .queryParam("email", email)
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(Boolean.class)
            .returnResult()
            .getResponseBody();
    }

    private void deleteFraudUserByEmail(final String id) {
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "fraudUsers", id)
                .build())
            .exchange()
            .expectStatus().isOk();
    }

    private FraudUser postFraudUser(final FraudUser fraudUser, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder.pathSegment("api", "fraudUsers").build())
            .bodyValue(fraudUser)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(FraudUser.class)
            .returnResult()
            .getResponseBody();
    }

}
