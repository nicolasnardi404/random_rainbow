package com.randomrainbow.springboot.demosecurity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.randomrainbow.springboot.demosecurity.entity.User;

import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${brevo.api.key}")
    private String apiKeyBrevo;


    private TransactionalEmailsApi brevoApi;

    public EmailServiceImpl() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey("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");
        brevoApi = new TransactionalEmailsApi();
    }

    @Override
    public void sendVerificationEmail(User user, String token) {
        try {
            Long templateId = 2L; // Replace with your actual template ID
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

            // Set the recipient
            List<SendSmtpEmailTo> toList = List.of(new SendSmtpEmailTo().email(user.getEmail()));
            sendSmtpEmail.setTo(toList);

            // Set the template ID
            sendSmtpEmail.setTemplateId(templateId);

            // Add template variables
            Map<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("verify_link", "http://localhost:8080/api/verify?token=" + token);
            sendSmtpEmail.setParams(params);

            // Send the email
            brevoApi.sendTransacEmail(sendSmtpEmail);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            Long templateId = 3L; // Replace with your actual template ID
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

            // Set the recipient
            List<SendSmtpEmailTo> toList = List.of(new SendSmtpEmailTo().email(user.getEmail()));
            sendSmtpEmail.setTo(toList);

            // Set the template ID
            sendSmtpEmail.setTemplateId(templateId);

            // Add template variables
            Map<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("reset_link", "http://localhost:3000/new-password/" + resetToken);
            sendSmtpEmail.setParams(params);

            // Send the email
            brevoApi.sendTransacEmail(sendSmtpEmail);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
