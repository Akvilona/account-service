package ru.gmm.demo.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gmm.demo.model.api.FraudUser;

@RequiredArgsConstructor
public class FraudClient {

    private final WebClient webClient;

    /**
     * Checks fraud for the specified email address.
     *
     * @param email The user's email address for checking fraud.
     * @return true if the user is considered a fraud; otherwise, false.
     */
    public Boolean checkFraud(final String email) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("users")
                .queryParam("email", email)
                .build())
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();
    }

    /**
     * Sends information about the user to create such a user if there is no such user.
     *
     * @param fraudUser Information about the user to send the name and email of the fraudster to the server.
     * @return FraudUser returns information about the user received after adding the wallet to the server.
     */
    public FraudUser postFraudUserById(final FraudUser fraudUser) {
        return webClient.post()
            .uri("/users")
            .bodyValue(fraudUser)
            .retrieve()
            .bodyToMono(FraudUser.class)
            .block();
    }

    public void deleteFraudUserById(final Long id) {
        webClient.delete()
            .uri("/users/{id}", id)
            .retrieve()
            .toBodilessEntity()
            .block();
    }
}
