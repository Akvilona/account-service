package ru.gmm.demo.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.CreateTransactionRq;
import ru.gmm.demo.model.api.CreateTransactionRs;
import ru.gmm.demo.model.enums.AccountStatus;
import ru.gmm.demo.repository.AccountRepository;
import ru.gmm.demo.repository.TransactionRepository;
import ru.gmm.demo.repository.UserRepository;
import ru.gmm.demo.support.DatabaseAwareTestBase;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient(timeout = "360000")
class TransactionApiControllerTest extends DatabaseAwareTestBase {
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    protected String getSchema() {
        return "public";
    }

    @Override
    protected Set<String> getTables() {
        return Set.of("users", "accounts", "transactions");
    }

    @Test
    @Transactional
    @Disabled
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
            .accountTo(accountEntity.getId().toString())
            .accountFrom(accountEntity.getId().toString())
            .sum(new BigDecimal("1000.00"))
            .type(CreateTransactionRq.TypeEnum.DEPOSIT)
            .build();

        CreateTransactionRs createTransactionRs = createTransaction(request, 200);
        assertThat(createTransactionRs.getId()).isNotNull();
        assertThat(createTransactionRs.getAccountTo()).isEqualTo(accountEntity.getId().toString());
        assertThat(createTransactionRs.getSum()).isEqualTo(request.getSum());

        assertThat(transactionRepository.findAll())
            .hasSize(1)
            .first()
            .satisfies(transactionRepository -> {
                assertThat(transactionRepository.getId()).isNotNull();
                assertThat(transactionRepository.getSum()).isEqualTo("1000.00");
                assertThat(transactionRepository.getDescription()).isNull();
            });
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
