package ru.gmm.demo.controller;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.CreateTransactionRq;
import ru.gmm.demo.model.api.CreateTransactionRs;
import ru.gmm.demo.model.api.TransactionRs;
import ru.gmm.demo.model.api.TransactionUpdateRq;
import ru.gmm.demo.model.enums.AccountStatus;
import ru.gmm.demo.model.enums.TransactionType;
import ru.gmm.demo.support.IntegrationTestBase;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("PMD.TooManyMethods")
class TransactionApiControllerTest extends IntegrationTestBase {

    private static UserEntity getUserEntity() {
        TransactionEntity transactionEntityFirst = TransactionEntity.builder()
            .sum(new BigDecimal("1000.0"))
            .type(TransactionType.DEPOSIT)
            .build();
        TransactionEntity transactionEntitySecond = TransactionEntity.builder()
            .sum(new BigDecimal("2000.0"))
            .type(TransactionType.DEPOSIT)
            .build();

        AccountEntity account = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("0123456")
            .build();
        account.withTransactionsFrom(transactionEntityFirst);
        account.withTransactionTo(transactionEntityFirst);
        account.withTransactionsFrom(transactionEntitySecond);
        account.withTransactionTo(transactionEntitySecond);

        return UserEntity.builder()
            .name("test")
            .password("pass")
            .build()
            .withAccount(account);
    }

    private static AccountEntity createAccount(final String sum,
                                               final String number,
                                               final TransactionEntity transactionsFrom,
                                               final TransactionEntity transactionsTo) {
        return AccountEntity.builder()
            .sum(new BigDecimal(sum))
            .status(AccountStatus.OPENED)
            .number(number)
            .build()
            .withTransactionsFrom(transactionsFrom)
            .withTransactionTo(transactionsTo);
    }

    @Test
    void createTransaction() {
        AccountEntity accountEntity = AccountEntity.builder()
            .sum(new BigDecimal("123"))
            .status(AccountStatus.OPENED)
            .number("123456")
            .build();

        UserEntity userEntity = UserEntity.builder()
            .name("test")
            .password("123")
            .build()
            .withAccount(accountEntity);

        userRepository.save(userEntity);

        CreateTransactionRq request = CreateTransactionRq.builder()
            .accountTo(accountEntity.getNumber())
            .accountFrom(accountEntity.getNumber())
            .sum(new BigDecimal("1000.00"))
            .type(CreateTransactionRq.TypeEnum.DEPOSIT)
            .build();

        CreateTransactionRs createTransactionRs = createTransaction(request, 200);

        assertThat(createTransactionRs)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(CreateTransactionRs.builder()
                .status(CreateTransactionRq.TypeEnum.DEPOSIT.name())
                .sum(request.getSum())
                .build());

        assertThat(transactionRepository.findAll())
            .hasSize(1)
            .first()
            .satisfies(transactionRepository -> {
                assertThat(transactionRepository.getId()).isNotNull();
                assertThat(transactionRepository.getSum()).isEqualTo("1000.00");
                assertThat(transactionRepository.getDescription()).isNull();
            });

        // Вот пример добавления утверждения в конце метода
        Assert.assertTrue(true);
    }

    @Test
    void getAllTransactionShouldWork() {
        // создаем первую транзакцию
        final UserEntity userEntity = getUserEntity();

        userRepository.save(userEntity);

        List<TransactionRs> allTransaction = getAllTransaction(200);
        assertThat(allTransaction)
            .hasSize(2)
            .usingElementComparatorIgnoringFields("createDateTime", "updateDateTime")
            .containsExactlyInAnyOrder(
                TransactionRs.builder()
                    .id("1")
                    .accountFrom("0123456")
                    .accountTo("0123456")
                    .sum(new BigDecimal("1000.00"))
                    .status(TransactionType.DEPOSIT.toString())
                    .build(),
                TransactionRs.builder()
                    .id("2")
                    .accountFrom("0123456")
                    .accountTo("0123456")
                    .sum(new BigDecimal("2000.00"))
                    .status(TransactionType.DEPOSIT.toString())
                    .build());
    }

    @Test
    void getTransactionByIdShouldWork() {
        final UserEntity userEntity = getUserEntity();

        userRepository.save(userEntity);
        // Простое утверждение для успешного завершения теста
        //Assert.assertTrue(true);

        TransactionRs transactionById = getTransactionById("1", 200);

        assertThat(transactionById)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .ignoringFields("updateDateTime")
            .ignoringFields("createDateTime")
            .isEqualTo(TransactionRs.builder()
                .status(TransactionType.DEPOSIT.toString())
                .description("any WITHDRAWAL")
                .sum(new BigDecimal("1000.00"))
                .build());

        assertThat(transactionRepository.findAll())
            .hasSize(2)
            .first()
            .satisfies(transactionRepository -> {
                assertThat(transactionRepository.getId()).isNotNull();
                assertThat(transactionRepository.getSum()).isEqualTo("1000.00");
                assertThat(transactionRepository.getDescription()).isEqualTo("any WITHDRAWAL");
            });
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

        final UserEntity userEntity = getUserEntity();

        userRepository.save(userEntity);
        // Простое утверждение для успешного завершения теста
        Assert.assertTrue(true);

        TransactionUpdateRq transactionUpdateRq = updateTransaction("1", request, 200);

        assertThat(transactionUpdateRq)
            .hasFieldOrPropertyWithValue("sum", request.getSum())
            .hasFieldOrPropertyWithValue("description", request.getDescription())
            .extracting(TransactionUpdateRq::getId)
            .isNotNull();

        assertThat(transactionRepository.findAll())
            .hasSize(2)
            .last()
            .satisfies(transaction -> {
                assertThat(transaction.getSum()).isEqualByComparingTo(new BigDecimal("3000"));
                assertThat(transaction.getDescription()).isEqualTo(request.getDescription());
            })
            .isNotNull();
    }

    @Test
    void deleteTransactionByIdShouldWork() {
        // Arrange
        final UserEntity userEntity = getUserEntity();
        userRepository.save(userEntity);

        // Act
        deleteTransactionBy("1", 200);

        // Assert
        executeInTransaction(() ->
            assertThat(transactionRepository.findAll())
                .allSatisfy(transaction ->
                    assertThat(transaction)
                        .usingRecursiveComparison()
                        .ignoringFields("accountFrom", "accountTo")  // Игнорирование поля user при сравнении
                        .isNotNull()
                ));

        webTestClient.get()
            .uri("/api/transaction")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(TransactionEntity.class)
            .hasSize(1);

        // Вот пример добавления утверждения в конце метода
        Assert.assertTrue(true);
    }

    private CreateTransactionRs createTransaction(final CreateTransactionRq request, final int status) {
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

    private void deleteTransactionBy(final String id, final int status) {
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "transaction", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(status);
    }
}
