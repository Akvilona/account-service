package ru.gmm.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gmm.demo.exception.Result;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.AccountRegistrationRq;
import ru.gmm.demo.model.api.AccountRegistrationRs;
import ru.gmm.demo.model.api.AccountRs;
import ru.gmm.demo.model.enums.AccountStatus;
import ru.gmm.demo.repository.AccountRepository;
import ru.gmm.demo.repository.UserRepository;
import ru.gmm.demo.support.DatabaseAwareTestBase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AccountApiControllerTest extends DatabaseAwareTestBase {

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected String getSchema() {
        return "public";
    }

    @Override
    protected Set<String> getTables() {
        return Set.of("users", "accounts", "transactions");
    }

    @Test
    void createAccountSuccess() {
        UserEntity userEntity = UserEntity.builder()
            .name("test")
            .password("123")
            .build();
        userRepository.save(userEntity);

        AccountRegistrationRq registrationRq = AccountRegistrationRq.builder()
            .account("123456")
            .sum(BigDecimal.valueOf(1000))
            .description("comment any")
            .userId(userEntity.getId())
            .build();

        AccountRegistrationRs accountRegistrationRs = createAccount(registrationRq, 200);
        assertThat(accountRegistrationRs.getId())
            .isNotNull();
        assertThat(accountRegistrationRs.getSum())
            .isEqualTo(BigDecimal.valueOf(1000));

        assertThat(accountRepository.findAll())
            .hasSize(1)
            .first()
            .satisfies(account -> {
                assertThat(account.getNumber()).isEqualTo("123456");
                assertThat(account.getSum()).isEqualTo("1000.00");
                assertThat(account.getStatus()).isEqualTo(AccountStatus.OPENED);
                assertThat(account.getId()).isNotNull();
                assertThat(account.getUser()).isNotNull();
                assertThat(account.getAudit().getCreateDateTime()).isNotNull();
            });
    }

    @Test
    void createAccountShouldReturnErrCode002() {
        AccountRegistrationRq registrationRq = AccountRegistrationRq.builder()
            .account("123456")
            .sum(BigDecimal.valueOf(1000))
            .description("comment any")
            .userId(999L)
            .build();

        Result result = createAccountException(registrationRq, 400);
        assertThat(result.getCode()).isEqualTo("ERR.CODE.002");
        assertThat(result.getDescription()).isEqualTo("Пользователь с id 999 не найден");
    }

    @Test
    void getAllAccountSuccess() {
        UserEntity userEntity = UserEntity.builder()
            .name("test")
            .password("123")
            .build();
        userRepository.save(userEntity);

        AccountEntity account1 = AccountEntity.builder()
            .sum(BigDecimal.valueOf(123))
            .number("123456")
            .user(userEntity)
            .build();
        AccountEntity account2 = AccountEntity.builder()
            .sum(BigDecimal.valueOf(321))
            .number("654321")
            .user(userEntity)
            .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        assertThat(getAllAccount())
            .hasSize(2)
            .containsExactlyInAnyOrder(
                AccountRs.builder()
                    .id(account1.getId().toString())
                    .account(account1.getNumber())
                    .sum(new BigDecimal("123.00"))
                    .status(AccountRs.StatusEnum.OPENED)
                    .transactionsTo(List.of())
                    .transactionsFrom(List.of())
                    .build(),
                AccountRs.builder()
                    .id(account2.getId().toString())
                    .account(account2.getNumber())
                    .sum(new BigDecimal("321.00"))
                    .status(AccountRs.StatusEnum.OPENED)
                    .transactionsTo(List.of())
                    .transactionsFrom(List.of())
                    .build());
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

    private Result createAccountException(final AccountRegistrationRq request, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "account")
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(Result.class)
            .returnResult()
            .getResponseBody();
    }

    private List<AccountRs> getAllAccount() {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "account")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<AccountRs>>() {
            })
            .returnResult()
            .getResponseBody();
    }
}
