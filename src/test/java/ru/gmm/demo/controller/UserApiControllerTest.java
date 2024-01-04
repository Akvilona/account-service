package ru.gmm.demo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gmm.demo.client.FraudClient;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.model.api.UserRs;
import ru.gmm.demo.model.api.UserUpdateRq;
import ru.gmm.demo.model.support.BaseEntity;
import ru.gmm.demo.repository.UserRepository;
import ru.gmm.demo.support.DatabaseAwareTestBase;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class UserApiControllerTest extends DatabaseAwareTestBase {

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private FraudClient fraudClient;

    @Override
    protected String getSchema() {
        return "public";
    }

    @Override
    protected Set<String> getTables() {
        return Set.of("users", "accounts", "transactions");
    }

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

    @Test
    void getUserById() {
        UserEntity userEntity1 = UserEntity.builder()
            .id(1L)
            .name("test1_name")
            .email("test1@email.com")
            .surname("test1_surname")
            .password("test1_password")
            .build();
        UserEntity userEntity2 = UserEntity.builder()
            .id(2L)
            .name("test2_name")
            .email("test2@email.com")
            .surname("test2_surname")
            .password("test2_password")
            .build();

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);

        UserRs userRs = getUserById(userEntity2.getId().toString(), 200);

        assertThat(userRs)
            .hasFieldOrPropertyWithValue("id", userEntity2.getId().toString())
            .hasFieldOrPropertyWithValue("name", userEntity2.getName())
            .hasFieldOrPropertyWithValue("email", userEntity2.getEmail())
            .hasFieldOrPropertyWithValue("surname", userEntity2.getSurname())
            .isNotNull();

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

    @Test
    void updateUser() {
        UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .name("test1_name")
            .email("test1@email.com")
            .surname("test1_surname")
            .password("test1_password")
            .build();

        userRepository.save(userEntity);

        UserUpdateRq userUpdateRq = new UserUpdateRq("vasya", "ivanov");

        // Обновление имени и фамилии пользователя
        UserUpdateRq userUpdateRq1 = updateUser(userEntity.getId().toString(), userUpdateRq, 200);

        // Проверки на успешное обновление
        assertThat(userUpdateRq1)
            .hasFieldOrPropertyWithValue("name", userUpdateRq.getName())
            .hasFieldOrPropertyWithValue("surname", userUpdateRq.getSurname())
            .isNotNull();
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
}
