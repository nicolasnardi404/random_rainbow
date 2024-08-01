package com.randomrainbow.springboot.demosecurity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sendinblue.ApiClient;

import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailTo;

@Configuration
public class SendinblueConfig {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Bean
    public ApiClient apiClient() {
        ApiClient defaultClient = new ApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(apiKey);
        return defaultClient;
    }

    @Bean
    public TransactionalEmailsApi transactionalEmailsApi() {
        ApiClient defaultClient = apiClient(); // Use the consolidated ApiClient bean
        return new TransactionalEmailsApi(defaultClient);
    }
}
