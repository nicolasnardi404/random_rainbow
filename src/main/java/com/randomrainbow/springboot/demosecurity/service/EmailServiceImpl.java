package com.randomrainbow.springboot.demosecurity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

    private final Environment environment;
    private TransactionalEmailsApi brevoApi;

     @Autowired
    public EmailServiceImpl(Environment environment) {
        this.environment = environment;
        initializeBrevoApi();
    }


   private void initializeBrevoApi() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(environment.getProperty("BREVO_API_KEY"));
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
            params.put("verify_link", "http://www.randomrainbow.art/email-verified/" + token);
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
            params.put("reset_link", "http://www.randomrainbow.art/new-password/" + resetToken);
            sendSmtpEmail.setParams(params);

            // Send the email
            brevoApi.sendTransacEmail(sendSmtpEmail);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
