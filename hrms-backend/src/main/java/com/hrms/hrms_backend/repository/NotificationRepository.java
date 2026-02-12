package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.Notification;
import com.hrms.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Integer countNotificationByUserAndIsReadIsFalse(User user);

    List<Notification> getNotificationsByUserAndIsReadFalse(User user);

    List<Notification> getNotificationsByUser(User user);

}
