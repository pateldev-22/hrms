package com.hrms.hrms_backend.controller;

import com.hrms.hrms_backend.dto.notification.NotificationResponse;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.service.NotificationService;
import com.hrms.hrms_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public List<NotificationResponse> getNotifications(){
        User user = getCurrentUser();
        return notificationService.getNotificationByUser(user);
    }

    @GetMapping("/unread")
    public Integer getUnreadNotificationByUser(){
        User user = getCurrentUser();
        return notificationService.getUnreadNotificationByUser(user);
    }

    @DeleteMapping("/{id}")
    public String deleteNotification(@PathVariable Long id){
        return notificationService.deleteNotificationByUser(id);
    }

    @PutMapping("/{id}")
    public void markNotificationAsRead(@PathVariable Long id){
        notificationService.markAsRead(id);
    }

    private User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(userEmail);
    }
}
