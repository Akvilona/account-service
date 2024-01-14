package ru.gmm.demo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import ru.gmm.demo.exception.Result;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.AccountRs;
import ru.gmm.demo.model.api.UserAccountRs;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.model.api.UserRs;
import ru.gmm.demo.model.api.UserUpdateRq;
import ru.gmm.demo.model.support.BaseEntity;
import ru.gmm.demo.support.IntegrationTestBase;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings({"IllegalMethodCall", "PMD.TooManyMethods", "PMD.JUnitTestsShouldIncludeAssert"})
class UserApiControllerTest extends IntegrationTestBase {

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

    @Test
    void getAllUsersSuccess() {
        UserEntity userEntity1 = UserEntity.builder()
            .name("test1_name")
            .email("test1@email.com")
            .surname("test1_surname")
            .password("test1_password")
            .build();
        UserEntity userEntity2 = UserEntity.builder()
            .name("test2_name")
            .email("test2@email.com")
            .surname("test2_surname")
            .password("test2_password")
            .build();

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);

        assertThat(getAllUsers())
            .hasSize(2)
            .containsExactlyInAnyOrder(
                UserRs.builder()
                    .id(userEntity1.getId().toString())
                    .name(userEntity1.getName())
                    .surname(userEntity1.getSurname())
                    .email(userEntity1.getEmail())
                    .build(),
                UserRs.builder()
                    .id(userEntity2.getId().toString())
                    .name(userEntity2.getName())
                    .surname(userEntity2.getSurname())
                    .email(userEntity2.getEmail())
                    .build()
            );
    }

    @Test
    void getUserByIdShouldWork() {
        UserEntity userEntity2 = UserEntity.builder()
            .id(2L)
            .name("test2_name")
            .email("test2@email.com")
            .surname("test2_surname")
            .password("test2_password")
            .build();

        userRepository.save(userEntity2);

        assertThat(getUserById(userEntity2.getId().toString(), 200))
            .hasFieldOrPropertyWithValue("id", userEntity2.getId().toString())
            .hasFieldOrPropertyWithValue("name", userEntity2.getName())
            .hasFieldOrPropertyWithValue("email", userEntity2.getEmail())
            .hasFieldOrPropertyWithValue("surname", userEntity2.getSurname());
    }

    @Test
    void getUserByIdShouldReturnErrCode002() {
        assertThat(getUserByIdError("999", 400))
            .isEqualTo(Result.builder()
                .code("ERR.CODE.002")
                .description("Пользователь с id 999 не найден")
                .build());

    }

    @Test
    void updateUserShouldWork() {
        UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .name("test1_name")
            .email("test1@email.com")
            .surname("test1_surname")
            .password("test1_password")
            .build();

        userRepository.save(userEntity);

        UserUpdateRq request = UserUpdateRq.builder()
            .name("vasya")
            .surname("ivanov")
            .build();

        // Обновление имени и фамилии пользователя
        UserUpdateRq response = updateUser(userEntity.getId().toString(), request, 200);

        // Проверки на успешное обновление
        assertThat(response)
            .hasFieldOrPropertyWithValue("name", request.getName())
            .hasFieldOrPropertyWithValue("surname", request.getSurname())
            .isNotNull();
    }

    @Test
    void updateUserShouldReturnErrCode002() {
        UserUpdateRq request = UserUpdateRq.builder()
            .name("vasya")
            .surname("ivanov")
            .build();

        assertThat(updateUserError("999", request, 400))
            .isEqualTo(Result.builder()
                .code("ERR.CODE.002")
                .description("Пользователь с id 999 не найден")
                .build());
    }

    @Test
    void findUserAccountsShouldWork() {
        UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .name("test1_name")
            .email("test_email")
            .surname("test1_surname")
            .password("test1_password")
            .build()
            .withAccount(AccountEntity.builder()
                .number("123456")
                .sum(new BigDecimal("1000.00"))
                .build())
            .withAccount(AccountEntity.builder()
                .number("4321")
                .sum(new BigDecimal("2000.00"))
                .build());

        userRepository.save(userEntity);

        assertThat(getUserAccountRs(userEntity.getId().toString(), 200))
            .hasFieldOrPropertyWithValue("id", userEntity.getId().toString())
            .hasFieldOrPropertyWithValue("name", userEntity.getName())
            .hasFieldOrPropertyWithValue("email", userEntity.getEmail())
            .hasFieldOrPropertyWithValue("surname", userEntity.getSurname())
            .extracting(UserAccountRs::getAccounts)
            .asList()
            .hasSize(2)
            .containsExactlyInAnyOrder(
                AccountRs.builder()
                    .id(userEntity.getAccounts().get(0).getId().toString())
                    .account(userEntity.getAccounts().get(0).getNumber())
                    .sum(userEntity.getAccounts().get(0).getSum())
                    .status(AccountRs.StatusEnum.OPENED)
                    .transactionsFrom(List.of())
                    .transactionsTo(List.of())
                    .build(),
                AccountRs.builder()
                    .id(userEntity.getAccounts().get(1).getId().toString())
                    .account(userEntity.getAccounts().get(1).getNumber())
                    .sum(userEntity.getAccounts().get(1).getSum())
                    .status(AccountRs.StatusEnum.OPENED)
                    .transactionsFrom(List.of())
                    .transactionsTo(List.of())
                    .build()
            );

    }

    @Test
    void findUserAccountsShouldReturnErrCode002() {
        assertThat(getUserAccountRsError("999", 400))
            .isEqualTo(Result.builder()
                .code("ERR.CODE.002")
                .description("Пользователь с id 999 не найден")
                .build());
    }

    @Test
    void deleteUserByIdShouldWorkWithOneUser() {
        UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .name("test1_name")
            .email("test_email")
            .surname("test1_surname")
            .password("test1_password")
            .build();

        userRepository.save(userEntity);

        deleteUser(userEntity.getId().toString());

        assertThat(userRepository.findAll())
            .isEmpty();
    }

    @Test
    void deleteUserByIdShouldWorkWithMultipleUsers() {
        UserEntity userEntity = UserEntity.builder()
            .name("test1_name")
            .email("test_email")
            .surname("test1_surname")
            .password("test1_password")
            .build();
        UserEntity userEntity2 = UserEntity.builder()
            .name("test2_name")
            .email("test2_email")
            .surname("test2_surname")
            .password("test2_password")
            .build();

        userRepository.saveAll(List.of(userEntity, userEntity2));

        deleteUser(userEntity.getId().toString());

        executeInTransaction(() ->
            assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                // .ignoringFields("accounts")
                .isEqualTo(userEntity2));
    }

    private void deleteUser(final String id) {
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users", id)
                .build())
            .exchange()
            .expectStatus().isOk();
    }

    private UserUpdateRq updateUser(final String id, final UserUpdateRq userUpdateRq, final int status) {
        return webTestClient.put()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users", id)
                .build())
            .bodyValue(userUpdateRq)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(UserUpdateRq.class)
            .returnResult()
            .getResponseBody();
    }

    private Result updateUserError(final String id, final UserUpdateRq userUpdateRq, final int status) {
        return webTestClient.put()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users", id)
                .build())
            .bodyValue(userUpdateRq)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(Result.class)
            .returnResult()
            .getResponseBody();
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

    private UserRs getUserById(final String id, final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(UserRs.class)
            .returnResult()
            .getResponseBody();
    }

    private Result getUserByIdError(final String id, final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(Result.class)
            .returnResult()
            .getResponseBody();
    }

    private List<UserRs> getAllUsers() {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<UserRs>>() {
            })
            .returnResult()
            .getResponseBody();

    }

    private UserAccountRs getUserAccountRs(final String id, final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users", id, "accounts")
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(UserAccountRs.class)
            .returnResult()
            .getResponseBody();
    }

    private Result getUserAccountRsError(final String id, final int status) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "users", id, "accounts")
                .build())
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(Result.class)
            .returnResult()
            .getResponseBody();
    }

}
