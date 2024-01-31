package com.account.accountservice.controller;

import com.account.accountservice.model.UserEntity;
import com.account.accountservice.model.enums.TransactionType;
import com.account.accountservice.support.DataProvider;
import com.account.accountservice.support.IntegrationTestBase;
import com.openapi.accountservice.server.model.api.CreateTransactionRq;
import com.openapi.accountservice.server.model.api.CreateTransactionRs;
import com.openapi.accountservice.server.model.api.TransactionRs;
import com.openapi.accountservice.server.model.api.TransactionUpdateRq;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert", "PMD.TooManyMethods"})
class TransactionApiControllerTest extends IntegrationTestBase {

    @Test
    void getAllTransactionShouldWorkFiveUsers() {
        List<UserEntity> list = List.of(
            DataProvider.getUserEntity("user0", "pass0", "01234560"),
            DataProvider.getUserEntity("user1", "pass1", "01234561"),
            DataProvider.getUserEntity("user2", "pass2", "01234562"),
            DataProvider.getUserEntity("user3", "pass3", "01234563"),
            DataProvider.getUserEntity("user4", "pass4", "01234564")
        );
        userRepository.saveAll(list);

        List<TransactionRs> allTransaction = getAllTransaction(200);
        assertThat(allTransaction)
            .hasSize(5);
    }

    @Test
    void getAllTransactionShouldWork() {
        final UserEntity userEntity = DataProvider.getUserEntity();

        userRepository.save(userEntity);

        List<TransactionRs> allTransaction = getAllTransaction(200);

        assertThat(allTransaction)
            .hasSize(4)
            .usingElementComparatorIgnoringFields("createDateTime", "updateDateTime")
            .containsExactlyInAnyOrder(
                TransactionRs.builder()
                    .id("1")
                    .accountFrom("0123456")
                    .accountTo("1234567")
                    .sum(new BigDecimal("3000.00"))
                    .status("TRANSFER")
                    .build(),
                TransactionRs.builder()
                    .id("2")
                    .accountTo("0123456")
                    .sum(new BigDecimal("2000.00"))
                    .status("DEPOSIT")
                    .build(),
                TransactionRs.builder()
                    .id("3")
                    .accountTo("0123456")
                    .sum(new BigDecimal("2000.00"))
                    .status("DEPOSIT")
                    .build(),
                TransactionRs.builder()
                    .id("4")
                    .accountFrom("1234567")
                    .sum(new BigDecimal("1000.00"))
                    .status("WITHDRAWAL")
                    .build()
            );

    }

    @Test
    void getTransactionByIdShouldWork() {
        final UserEntity userEntity = DataProvider.getUserEntity();

        userRepository.save(userEntity);

        TransactionRs transactionById = getTransactionById("1", 200);

        assertThat(transactionById)
            .usingRecursiveComparison()
            .ignoringFields("updateDateTime", "createDateTime")
            .isEqualTo(TransactionRs.builder()
                .id("1")
                .accountFrom("0123456")
                .accountTo("1234567")
                .sum(new BigDecimal("3000.00"))
                .status(TransactionType.TRANSFER.toString())
                .build());
    }

    @Test
    void updateTransactionShouldWork() {
        TransactionUpdateRq request = TransactionUpdateRq.builder()
            .id("1")
            .accountFrom("0123456")
            .accountTo("1234567")
            .sum(new BigDecimal(3000))
            .status(TransactionType.WITHDRAWAL.toString())
            .description("any world")
            .build();

        final UserEntity userEntity = DataProvider.getUserEntity();

        userRepository.save(userEntity);

        TransactionUpdateRq transactionUpdateRq = updateTransaction("1", request, 200);

        assertThat(transactionUpdateRq)
            .hasFieldOrPropertyWithValue("sum", request.getSum())
            .hasFieldOrPropertyWithValue("description", request.getDescription())
            .extracting(TransactionUpdateRq::getId)
            .isNotNull();

        assertThat(transactionRepository.findAll())
            .hasSize(4)
            .last()
            .satisfies(transaction -> {
                assertThat(transaction.getSum()).isEqualByComparingTo(new BigDecimal("3000"));
                assertThat(transaction.getDescription()).isEqualTo(request.getDescription());
            })
            .isNotNull();
    }

    @Test
    void grtTransactionByIdErrCode003() {
        final UserEntity userEntity = DataProvider.getUserEntity();

        userRepository.save(userEntity);

        TransactionRs transactionById = getTransactionById("1000", 400);

        assertThat(transactionById)
            .extracting(TransactionRs::getDescription)
            .isEqualTo("Счет с id 1000 не найден");

    }

    @Test
    void createTransactionErrCode003Deposit() {
        CreateTransactionRq createTransactionRqDeposit = CreateTransactionRq.builder()
            .accountFrom("0123456")
            .accountTo("0123456_999")
            .sum(new BigDecimal(3000))
            .type(CreateTransactionRq.TypeEnum.DEPOSIT)
            .description("any world")
            .build();

        CreateTransactionRs transactionRegistrationRs = createWithdrawalTransaction(createTransactionRqDeposit, 400);

        assertThat(transactionRegistrationRs)
            .extracting(CreateTransactionRs::getDescription)
            .isEqualTo("Счет с id 0123456_999 не найден");

    }

    @Test
    void createTransactionErrCode003Withdrawal() {
        CreateTransactionRq createTransactionRqDeposit = CreateTransactionRq.builder()
            .accountFrom("0123456")
            .accountTo("0123456_999")
            .sum(new BigDecimal(3000))
            .type(CreateTransactionRq.TypeEnum.WITHDRAWAL)
            .description("any world")
            .build();

        CreateTransactionRs transactionRegistrationRs = createWithdrawalTransaction(createTransactionRqDeposit, 400);

        assertThat(transactionRegistrationRs)
            .extracting(CreateTransactionRs::getDescription)
            .isEqualTo("Счет с id 0123456_999 не найден");

    }

    @Test
    void deleteTransactionByIdShouldWork() {
        final UserEntity userEntity = DataProvider.getUserEntity();
        userRepository.save(userEntity);

        deleteTransactionBy("1", 200);

        executeInTransaction(() ->
            assertThat(transactionRepository.findAll())
                .allSatisfy(transaction ->
                    assertThat(transaction)
                        .usingRecursiveComparison()
                        .ignoringFields("accountFrom", "accountTo")  // Игнорирование поля user при сравнении
                        .isNotNull()
                ));
    }

    private TransactionRs getTransactionById(final String id, final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "transaction", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(TransactionRs.class)
            .returnResult()
            .getResponseBody();
    }

    private TransactionUpdateRq updateTransaction(final String id, final TransactionUpdateRq request, final int status) {
        return webTestClient.put()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "transaction", id)
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(TransactionUpdateRq.class)
            .returnResult()
            .getResponseBody();
    }

    private CreateTransactionRs createWithdrawalTransaction(final CreateTransactionRq request, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "transaction")
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(CreateTransactionRs.class)
            .returnResult()
            .getResponseBody();
    }

    private void deleteTransactionBy(final String id, final int status) {
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "transaction", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(status);
    }

    private List<TransactionRs> getAllTransaction(final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "transaction")
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(new ParameterizedTypeReference<List<TransactionRs>>() {
            })
            .returnResult()
            .getResponseBody();
    }
}
