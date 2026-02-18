package com.hrms.hrms_backend.event;

import com.hrms.hrms_backend.constants.EmailType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

@Getter
public class EmailEvent extends ApplicationEvent {

    private final List<String> recipients;
    private final EmailType emailType;
    private final Map<String, Object> data;
    private final String attachmentUrl;

    public EmailEvent(
            Object source,
            List<String> recipients,
            EmailType emailType,
            Map<String, Object> data,
            String attachmentUrl
    ) {
        super(source);
        this.recipients = recipients;
        this.emailType = emailType;
        this.data = data;
        this.attachmentUrl = attachmentUrl;
    }
}
