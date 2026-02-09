package com.hrms.hrms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "notification_type", nullable = false, length = 50)
    private String notificationType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message")
    private String message;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public static Notification createTravelAssignmentNotification(User user, TravelPlan travelPlan) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setNotificationType("TRAVEL_ASSIGNED");
        notification.setTitle("New Travel Assignment");
        notification.setMessage(String.format("You have been assigned to travel: %s to %s",
                travelPlan.getTravelName(), travelPlan.getDestination()));
        return notification;
    }
}

