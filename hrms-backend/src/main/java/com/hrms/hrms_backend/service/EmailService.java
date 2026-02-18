package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.constants.EmailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;

    @Autowired
    public EmailService(JavaMailSender mailSender, EmailTemplateService templateService) {
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    public void sendEmail(
            List<String> recipients,
            EmailType emailType,
            Map<String, Object> data,
            String attachmentUrl
    ) {
        try {
            String body = templateService.getEmailTemplate(emailType, data);

            String finalBody = attachmentUrl != null
                    ? body + "\nAttachment: " + attachmentUrl
                    : body;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipients.toArray(new String[0]));
            message.setText(finalBody);
            mailSender.send(message);
        } catch (Exception e) {
        }
    }
}
