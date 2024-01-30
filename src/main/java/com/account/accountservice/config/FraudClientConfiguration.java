package com.account.accountservice.config;

import com.openapi.fraudservice.client.api.FraudUserApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FraudClientConfiguration {

    @Bean
    public FraudUserApi fraudClient() {
        return new FraudUserApi();
    }
}
