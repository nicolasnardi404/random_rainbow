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
    
    @Value("${admin.email}")
    private String adminEmail;

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
            Long templateId = 4L; // Replace with your actual template ID
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
    
    @Override
    public void sendVideoApprovalEmail(User user, String videoTitle, String videoUrl) {
        try {
            Long templateId = 5L; // You'll need to create a template in Brevo for video approval
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

            // Set the recipient
            List<SendSmtpEmailTo> toList = List.of(new SendSmtpEmailTo().email(user.getEmail()));
            sendSmtpEmail.setTo(toList);

            // Set the template ID
            sendSmtpEmail.setTemplateId(templateId);

            // Add template variables
            Map<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("video_title", videoTitle);
            params.put("video_url", videoUrl);
            sendSmtpEmail.setParams(params);

            // Send the email
            brevoApi.sendTransacEmail(sendSmtpEmail);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send video approval email", e);
        }
    }
    
    @Override
    public void sendNewVideoNotificationToAdmin(User user, String videoTitle, String videoLink) {
        try {
            Long templateId = 6L; // You'll need to create a template in Brevo for admin notifications
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

            // Set the recipient (admin email)
            List<SendSmtpEmailTo> toList = List.of(new SendSmtpEmailTo().email(adminEmail));
            sendSmtpEmail.setTo(toList);

            // Set the template ID
            sendSmtpEmail.setTemplateId(templateId);

            // Add template variables
            Map<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("video_title", videoTitle);
            params.put("video_link", "http://www.randomrainbow.art/home/" + videoLink);
            params.put("admin_url", "http://www.randomrainbow.art/admin-controller");
            sendSmtpEmail.setParams(params);

            // Send the email
            brevoApi.sendTransacEmail(sendSmtpEmail);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send admin notification email", e);
        }
    }
}
