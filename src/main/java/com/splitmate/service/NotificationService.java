// File: NotificationService.java
package com.splitmate.service;

import java.util.List;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;

public interface NotificationService {
    void createNotification(Notification notification);
    void markAsRead(String notificationId);

    public Notification createNotification(Notification notification);
}
