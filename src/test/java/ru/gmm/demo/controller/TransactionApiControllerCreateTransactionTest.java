package ru.gmm.demo.controller;

import org.junit.jupiter.api.Test;
import ru.gmm.demo.exception.Result;
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
            .sum(new BigDecimal("123000"))
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

        //todo: СДЕЛАНО: добавить проверку что деньги начислились
        assertThat(accountRepository.findById(accountEntity.getId()))
            .get()
            .extracting(AccountEntity::getSum)
            .isEqualTo(new BigDecimal("124000.00"));
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

        //todo: СДЕЛАНО добавить проверку что деньги с другого счета с̶н̶я̶л̶и̶с̶ь̶ зачислились
        assertThat(accountRepository.findById(accountEntitySecond.getId()))
            .get()
            .extracting(AccountEntity::getSum)
            .isEqualTo(new BigDecimal("124000.00"));

        // деньги со счета снялись
        assertThat(accountRepository.findById(accountEntityFirst.getId()))
            .get()
            .extracting(AccountEntity::getSum)
            .isEqualTo(new BigDecimal("122000.00"));
    }

    @Test
    void createTransactionWithdrawalSuccessPath() {

        //todo: СДЕЛАНО добавить проверку что деньги со счета снялись
        AccountEntity accountEntityFist = AccountEntity.builder()
            .sum(new BigDecimal("123000.00"))
            .status(AccountStatus.OPENED)
            .number("123456")
            .build();

        AccountEntity accountEntitySecpmd = AccountEntity.builder()
            .sum(new BigDecimal("0.00"))
            .status(AccountStatus.CLOSED)
            .number("123457")
            .build();

        UserEntity userEntity = UserEntity.builder()
            .name("user")
            .password("123")
            .build()
            .withAccount(accountEntityFist)
            .withAccount(accountEntitySecpmd);

        userRepository.save(userEntity);

        CreateTransactionRq createTransactionRq = CreateTransactionRq.builder()
            .accountFrom(accountEntityFist.getNumber())
            .accountTo(accountEntitySecpmd.getNumber())
            .type(CreateTransactionRq.TypeEnum.WITHDRAWAL)
            .sum(new BigDecimal("1000.00"))
            .build();

        CreateTransactionRs createTransactionRs = createTransaction(createTransactionRq, 200);

        assertThat(createTransactionRs)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(CreateTransactionRs.builder()
                .status(CreateTransactionRq.TypeEnum.WITHDRAWAL.name())
                .sum(createTransactionRq.getSum())
                .build());

        assertThat(transactionRepository.findAll())
            .hasSize(1)
            .first()
            .satisfies(transactionRepository -> {
                assertThat(transactionRepository.getId()).isNotNull();
                assertThat(transactionRepository.getSum()).isEqualTo("1000.00");
                assertThat(transactionRepository.getType().toString()).isEqualTo(createTransactionRq.getType().toString());
            });

        assertThat(accountRepository.findById(accountEntityFist.getId()))
            .get()
            .extracting(AccountEntity::getSum)
            .isEqualTo(new BigDecimal("122000.00"));

        assertThat(accountRepository.findById(accountEntitySecpmd.getId()))
            .get()
            .extracting(AccountEntity::getSum)
            .isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void createTransactionTransferErrCode008() {
        //todo СДЕЛАНО: check if ERR_CODE_008 was throws
        AccountEntity accountEntity = AccountEntity.builder()
            .number("123456")
            .status(AccountStatus.OPENED)
            .sum(new BigDecimal("123000.00"))
            .build();

        UserEntity userEntity = UserEntity.builder()
            .name("user")
            .password("123")
            .build()
            .withAccount(accountEntity)
            .withAccount(accountEntity);

        userRepository.save(userEntity);

        CreateTransactionRq createTransactionRq = CreateTransactionRq.builder()
            .accountFrom(accountEntity.getNumber())
            .accountTo(accountEntity.getNumber())
            .type(CreateTransactionRq.TypeEnum.TRANSFER)
            .sum(new BigDecimal("1000.00"))
            .build();

        Result result = createTransactionCode008(createTransactionRq, 400);
        assertThat(result.getCode()).isEqualTo("ERR.CODE.008");

    }

    @Test
    void createTransactionTransferAccountFromNotExistErrCode003() {
        //todo СДЕЛАНО check if ERR_CODE_003 was throws
        AccountEntity accountEntity = AccountEntity.builder()
            .number("12345")
            .sum(new BigDecimal("123000.00"))
            .build();

        UserEntity userEntity = UserEntity.builder()
            .name("user")
            .password("123")
            .build()
            .withAccount(accountEntity)
            .withAccount(accountEntity);

        userRepository.save(userEntity);

        CreateTransactionRq transactionRq = CreateTransactionRq.builder()
            .accountFrom("11111")
            .accountTo("12345")
            .sum(new BigDecimal("1000.00"))
            .type(CreateTransactionRq.TypeEnum.DEPOSIT)
            .build();

        Result result = createTransactionCode008(transactionRq, 400);
        assertThat(result.getCode()).isEqualTo("ERR.CODE.003");
        assertThat(result.getDescription()).isEqualTo("Счет с id 11111 не найден");

    }

    @Test
    void createTransactionTransferNotEnoughMoneyErrCode004() {
        //todo СДЕЛАНО check if ERR_CODE_004 was throws
        AccountEntity accountEntityFrom = AccountEntity.builder()
            .sum(new BigDecimal("1000.00"))
            .number("12345")
            .build();

        AccountEntity accountEntityTo = AccountEntity.builder()
            .sum(new BigDecimal("0.00"))
            .number("12346")
            .build();

        UserEntity userEntity = UserEntity.builder()
            .name("user")
            .password("123456")
            .build()
            .withAccount(accountEntityFrom)
            .withAccount(accountEntityTo);

        userRepository.save(userEntity);

        CreateTransactionRq createTransactionRq = CreateTransactionRq.builder()
            .accountFrom(accountEntityFrom.getNumber())
            .accountTo(accountEntityTo.getNumber())
            .sum(new BigDecimal("2000.00"))
            .type(CreateTransactionRq.TypeEnum.DEPOSIT)
            .build();

        Result result = createTransactionCode008(createTransactionRq, 400);
        assertThat(result.getCode()).isEqualTo("ERR.CODE.004");
        assertThat(result.getDescription()).isEqualTo("Счет с id 12345 имеет недостаточно средств");
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

    private Result createTransactionCode008(final CreateTransactionRq request, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "transaction")
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(Result.class)
            .returnResult()
            .getResponseBody();
    }
}
