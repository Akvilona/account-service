package ru.gmm.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gmm.demo.client.FraudClient;

@Configuration
public class FraudClientConfiguration {

    @Bean
    public FraudClient fraudClient() {
        final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build();
        return new FraudClient(webClient);
    }
}
