package ru.gmm.demo.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class FraudClient {

    private final WebClient webClient;

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

}
