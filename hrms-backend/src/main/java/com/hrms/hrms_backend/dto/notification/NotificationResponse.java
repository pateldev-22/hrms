package com.hrms.hrms_backend.dto.notification;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long notificationId;

    private String notificationType;

    private String title;

    private String message;

    private Boolean isRead;

    private LocalDateTime createdAt;
}
