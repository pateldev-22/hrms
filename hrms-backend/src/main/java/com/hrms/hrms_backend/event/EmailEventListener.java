package com.hrms.hrms_backend.event;

import com.hrms.hrms_backend.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailEventListener {

    private final EmailService emailService;

    public EmailEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleEmailEvent(EmailEvent event) {

        emailService.sendEmail(
                event.getRecipients(),
                event.getEmailType(),
                event.getData(),
                event.getAttachmentUrl()
        );
    }
}
