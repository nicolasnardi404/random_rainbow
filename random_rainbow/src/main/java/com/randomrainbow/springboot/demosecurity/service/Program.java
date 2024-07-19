package com.randomrainbow.springboot.demosecurity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

@Service
public class Program {

    @Value("${brevo.api.key}")
    private String apiKeyBrevo;

    public static void main(String[] args) {
        new Program().sendEmail("nico.nardi@hotmail.com", "Nico Nardi");
    }

    public void sendEmail(String recipientEmail, String username) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure API key authorization: api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey("");

        try {
            TransactionalEmailsApi api = new TransactionalEmailsApi();
            Long templateId = 2L;
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

            // Set the recipient
            List<SendSmtpEmailTo> toList = new ArrayList<>();
            toList.add(new SendSmtpEmailTo().email(recipientEmail));
            sendSmtpEmail.setTo(toList);

            // Set the template ID
            sendSmtpEmail.setTemplateId(templateId);

            // Add template variables
            Map<String, Object> params = new HashMap<>();
            params.put("username", username);
            sendSmtpEmail.setParams(params);

            // Send the email
            api.sendTransacEmail(sendSmtpEmail);
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
