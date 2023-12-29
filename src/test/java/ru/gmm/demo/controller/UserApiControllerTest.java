package ru.gmm.demo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gmm.demo.client.FraudClient;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.model.support.BaseEntity;
import ru.gmm.demo.repository.UserRepository;
import ru.gmm.demo.support.DatabaseAwareTestBase;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class UserApiControllerTest extends DatabaseAwareTestBase {

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private FraudClient fraudClient;

    @Override
    protected String getSchema() {
        return "public";
    }

    @Override
    protected Set<String> getTables() {
        return Set.of("users", "accounts", "transactions");
    }

    @Test
    void createUser() {
        UserRegistrationRq request = UserRegistrationRq.builder()
            .email("test@mail.ru")
            .password("12345678")
            .build();

        Mockito.when(fraudClient.checkFraud(any()))
            .thenReturn(false);

        UserRegistrationRs userRegistrationRs = postUser(request, 200);

        assertThat(userRegistrationRs)
            .hasFieldOrPropertyWithValue("email", request.getEmail())
            .extracting(UserRegistrationRs::getId)
            .isNotNull();

        assertThat(userRepository.findAll())
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("email", request.getEmail())
            .hasFieldOrPropertyWithValue("password", request.getPassword())
            .extracting(BaseEntity::getId)
            .isNotNull();
    }

    private UserRegistrationRs postUser(final UserRegistrationRq request, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users")
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(UserRegistrationRs.class)
            .returnResult()
            .getResponseBody();
    }
}
