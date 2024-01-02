package ru.gmm.demo.controller.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gmm.demo.model.api.AccountRegistrationRq;
import ru.gmm.demo.model.api.AccountRegistrationRs;
import ru.gmm.demo.support.DatabaseAwareTestBase;

import java.math.BigDecimal;
import java.util.Set;


class AccountApiTest extends DatabaseAwareTestBase {

    @Autowired
    protected WebTestClient webTestClient;
    @Override
    protected String getSchema() {
        return "public";
    }

    @Override
    protected Set<String> getTables() {
        return Set.of("users", "account", "transaction");
    }

    @Test
    void createAccount() {
        AccountRegistrationRq registrationRq = AccountRegistrationRq.builder()
            .account("123456")
            .sum(BigDecimal.valueOf(1000))
            .description("comment any")
            .build();

        AccountRegistrationRs accountRegistrationRs = createAccount(registrationRq, 200);

        Assertions.assertThat(accountRegistrationRs).isNotNull();
    }

    private AccountRegistrationRs createAccount(final AccountRegistrationRq request, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "account")
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(AccountRegistrationRs.class)
            .returnResult()
            .getResponseBody();
    }
}
