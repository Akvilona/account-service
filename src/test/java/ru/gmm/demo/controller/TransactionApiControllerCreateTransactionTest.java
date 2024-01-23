package ru.gmm.demo.controller;

import org.junit.jupiter.api.Test;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.CreateTransactionRq;
import ru.gmm.demo.model.api.CreateTransactionRs;
import ru.gmm.demo.model.enums.AccountStatus;
import ru.gmm.demo.support.IntegrationTestBase;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
class TransactionApiControllerCreateTransactionTest extends IntegrationTestBase {

    @Test
    void createTransactionDepositSuccessPath() {
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

        //todo: добавить проверку что деньги начислились
    }

    @Test
    void createTransactionTransferSuccessPath() {
        AccountEntity accountEntityFirst = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("123456")
            .build();

        AccountEntity accountEntitySecond = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("123457")
            .build();

        UserEntity userEntity = UserEntity.builder()
            .name("test")
            .password("123")
            .build()
            .withAccount(accountEntityFirst)
            .withAccount(accountEntitySecond);

        userRepository.save(userEntity);

        CreateTransactionRq request = CreateTransactionRq.builder()
            .accountFrom(accountEntityFirst.getNumber())
            .accountTo(accountEntitySecond.getNumber())
            .sum(new BigDecimal("1000.00"))
            .type(CreateTransactionRq.TypeEnum.TRANSFER)
            .build();

        CreateTransactionRs createTransactionRs = createTransaction(request, 200);

        assertThat(createTransactionRs)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(CreateTransactionRs.builder()
                .status(CreateTransactionRq.TypeEnum.TRANSFER.name())
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

        assertThat(accountRepository.findById(accountEntityFirst.getId()))
            .get()
            .extracting(AccountEntity::getSum)
            .isEqualTo(new BigDecimal("122000.00"));

        //todo: добавить проверку что деньги с другого счета снялись
    }

    @Test
    void createTransactionWithdrawalSuccessPath() {

        //todo: добавить проверку что деньги со счета снялись
    }

    @Test
    void createTransactionTransferErrCode008() {
        //todo check if ERR_CODE_008 was throws
    }

    @Test
    void createTransactionTransferAccountFromNotExistErrCode003() {
        //todo check if ERR_CODE_003 was throws
    }

    @Test
    void createTransactionTransferNotEnoughMoneyErrCode004() {
        //todo check if ERR_CODE_004 was throws
    }

    @Test
    void createTransactionTransferAccountToNotExistErrCode003() {
        //todo check if ERR_CODE_003 was throws
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

}
