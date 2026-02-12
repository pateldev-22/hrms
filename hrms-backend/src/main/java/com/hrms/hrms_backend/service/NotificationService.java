package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.dto.notification.NotificationResponse;
import com.hrms.hrms_backend.entity.Notification;
import com.hrms.hrms_backend.entity.TravelPlan;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public List<NotificationResponse> getAllNotifications(User user){
        List<Notification> notifications = notificationRepository.getNotificationsByUser(user);
        return notifications.stream().map(this::toNotificationResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createTravelAssignmentNotification(TravelPlan travel, User user){
        Notification notification = Notification.createTravelAssignmentNotification(user,travel);
        notificationRepository.save(notification);
    }

    @Transactional
    public Integer getUnreadNotificationByUser(User user){
        return notificationRepository.countNotificationByUserAndIsReadIsFalse(user);
    }

    @Transactional
    public List<NotificationResponse> getNotificationByUser(User user){
        List<Notification> notifications = notificationRepository.getNotificationsByUserAndIsReadFalse(user);
        return notifications.stream()
                .map(this::toNotificationResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public String deleteNotificationByUser(Long notificationId){
        notificationRepository.deleteById(notificationId);
        return "Successfully delete the notification";
    }

    @Transactional
    public String markAsRead(Long notificationId){
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException("notification not found yaar"));
        n.setRead(true);
        return "Marked As Read";
    }


    public NotificationResponse toNotificationResponseDto(Notification notification){
        NotificationResponse dto = new NotificationResponse();
        dto.setNotificationId(notification.getNotificationId());
        dto.setNotificationType(notification.getNotificationType());
        dto.setMessage(notification.getMessage());
        dto.setTitle(notification.getTitle());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setIsRead(notification.isRead());
        return dto;
    }
}
