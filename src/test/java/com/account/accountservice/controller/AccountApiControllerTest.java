package com.account.accountservice.controller;

import com.account.accountservice.exception.Result;
import com.account.accountservice.model.AccountEntity;
import com.account.accountservice.model.UserEntity;
import com.account.accountservice.model.enums.AccountStatus;
import com.account.accountservice.support.IntegrationTestBase;
import com.openapi.accountservice.server.model.api.AccountRegistrationRq;
import com.openapi.accountservice.server.model.api.AccountRegistrationRs;
import com.openapi.accountservice.server.model.api.AccountRs;
import com.openapi.accountservice.server.model.api.AccountUpdateRq;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.JUnitTestsShouldIncludeAssert"})
class AccountApiControllerTest extends IntegrationTestBase {

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
        assertThat(accountRegistrationRs.getId()).isNotNull();
        assertThat(accountRegistrationRs.getSum()).isEqualTo(BigDecimal.valueOf(1000));

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
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void deleteAccountByIdShouldWork() {
        UserEntity userEntity = UserEntity.builder()
            .name("test")
            .password("123")
            .build();
        userRepository.save(userEntity);

        AccountEntity account1 = AccountEntity.builder()
            .sum(new BigDecimal("123"))
            .number("123456")
            .user(userEntity)
            .build();
        AccountEntity account2 = AccountEntity.builder()
            .sum(new BigDecimal("321.00"))
            .number("654321")
            .user(userEntity)
            .build();

        accountRepository.saveAll(List.of(account1, account2));

        deleteAccount(account1.getId().toString());

        executeInTransaction(() ->
            assertThat(accountRepository.findAll())
                .allSatisfy(account ->
                    assertThat(account)
                        .usingRecursiveComparison()
                        .ignoringFields("user")  // Игнорирование поля user при сравнении
                        .isEqualTo(account2)
                )
        );
    }

    private void deleteAccount(final String id) {
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "account", id)
                .build())
            .exchange()
            .expectStatus().isOk();
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

    @Test
    void getAccountByIdShouldWork() {
        UserEntity userEntity = UserEntity.builder()
            .name("test")
            .password("123")
            .build();
        userRepository.save(userEntity);

        AccountEntity account = AccountEntity.builder()
            .sum(new BigDecimal("123.00"))
            .number("123456")
            .user(userEntity)
            .build();

        accountRepository.save(account);

        assertThat(getAccountById(account.getId().toString(), 200))
            .hasFieldOrPropertyWithValue("id", account.getId().toString())
            .hasFieldOrPropertyWithValue("account", account.getNumber())
            .hasFieldOrPropertyWithValue("sum", account.getSum());

    }

    @Test
    void getAccountByIdShouldErrCode007() {

        Result result = getAccountByIdCode007("999", 400);
        assertThat(result.getCode()).isEqualTo("ERR.CODE.007");
        assertThat(result.getDescription()).isEqualTo("Счет с id 999 не найден");

    }

    @Test
    void updateAccountShouldWork() {
        UserEntity userEntity = UserEntity.builder()
            .name("test")
            .password("123")
            .build();
        userRepository.save(userEntity);

        AccountEntity account = AccountEntity.builder()
            .sum(new BigDecimal("123.00"))
            .number("123456")
            .user(userEntity)
            .build();

        accountRepository.save(account);

        AccountUpdateRq request = AccountUpdateRq.builder()
            .account("654321")
            .sum(new BigDecimal("321.00"))
            .status(AccountUpdateRq.StatusEnum.CLOSED)
            .build();

        AccountUpdateRq accountUpdateRq = updateAccount(account.getId().toString(), request, 200);
        assertThat(accountUpdateRq.getAccount()).isEqualTo(request.getAccount());
        assertThat(accountUpdateRq.getSum()).isEqualTo(request.getSum());
    }

    private AccountUpdateRq updateAccount(final String id, final AccountUpdateRq request, final int status) {
        return webTestClient.put()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "account", id)
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(AccountUpdateRq.class)
            .returnResult()
            .getResponseBody();

    }

    private Result getAccountByIdCode007(final String id, final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "account", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(Result.class)
            .returnResult()
            .getResponseBody();
    }

    private AccountRs getAccountById(final String id, final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "account", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(AccountRs.class)
            .returnResult()
            .getResponseBody();
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
