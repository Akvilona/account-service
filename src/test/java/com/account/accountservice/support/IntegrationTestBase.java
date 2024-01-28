package com.account.accountservice.support;

import com.account.accountservice.repository.AccountRepository;
import com.account.accountservice.repository.TransactionRepository;
import com.account.accountservice.repository.UserRepository;
import com.froud.fraudservice.client.dto.FraudUserFraudRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;

@AutoConfigureWebTestClient(timeout = "360000")
public class IntegrationTestBase extends DatabaseAwareTestBase {
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected TransactionRepository transactionRepository;

    @MockBean
    protected FraudUserFraudRequest fraudClient;

    @Override
    protected String getSchema() {
        return "public";
    }

    @Override
    protected Set<String> getTables() {
        return Set.of("users", "accounts", "transactions");
    }
}
