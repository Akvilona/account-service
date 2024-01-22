package ru.gmm.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.CreateTransactionRq;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.enums.AccountStatus;
import ru.gmm.demo.model.enums.TransactionType;
import ru.gmm.demo.repository.AccountRepository;
import ru.gmm.demo.repository.TransactionRepository;
import ru.gmm.demo.support.IntegrationTestBase;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TransactionApiServiceTest extends IntegrationTestBase {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock  // Assuming you need to mock AccountRepository as well
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionApiService transactionApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTransaction() {
        // Arrange

        UserEntity userEntityRq = UserEntity.builder()
            .name("test")
            .password("123")
            .build();

        AccountEntity account1 = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("11111")
            .user(userEntityRq)
            .build();

        AccountEntity account2 = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("22222")
            .user(userEntityRq)
            .build();

        CreateTransactionRq createTransactionRq = CreateTransactionRq.builder()
            .sum(new BigDecimal("123000"))
            .type(CreateTransactionRq.TypeEnum.TRANSFER)
            .accountFrom(String.valueOf(account1))
            .accountTo(String.valueOf(account2))
            .build();

        // создаем первую транзакцию
        final UserEntity userEntity = getUserEntity();

        userRepository.save(userEntity);

        // Mock behavior of transactionRepository
        //when(transactionRepository.save(any())).thenReturn(new TransactionEntity());
        //userRepository.save(userEntity);

        // Act
        TransactionEntity result = transactionApiService.createTransaction(createTransactionRq);

        // Assert
        assertThat(result, is(notNullValue()));

        // Verify
        // verify(transactionRepository, times(1)).save(any());
    }

    private static UserEntity getUserEntity() {
        TransactionEntity transactionEntity1 = TransactionEntity.builder()
            .sum(new BigDecimal("2000.0"))
            .type(TransactionType.DEPOSIT)
            .build();
        TransactionEntity transactionEntity2 = TransactionEntity.builder()
            .sum(new BigDecimal("2000.0"))
            .type(TransactionType.DEPOSIT)
            .build();
        TransactionEntity transactionEntity3 = TransactionEntity.builder()
            .sum(new BigDecimal("3000.0"))
            .type(TransactionType.TRANSFER)
            .build();
        TransactionEntity transactionEntity4 = TransactionEntity.builder()
            .sum(new BigDecimal("1000.0"))
            .type(TransactionType.WITHDRAWAL)
            .build();

        AccountEntity account1 = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("0123456")
            .build();
        AccountEntity account2 = AccountEntity.builder()
            .sum(new BigDecimal("123000"))
            .status(AccountStatus.OPENED)
            .number("1234567")
            .build();

        account1.withTransactionTo(transactionEntity1);
        account1.withTransactionTo(transactionEntity2);
        account1.withTransactionsFrom(transactionEntity3);
        account2.withTransactionTo(transactionEntity3);
        account2.withTransactionsFrom(transactionEntity4);

        return UserEntity.builder()
            .name("test")
            .password("pass")
            .build()
            .withAccount(account1)
            .withAccount(account2);
    }
}
