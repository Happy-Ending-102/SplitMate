// File: NotificationService.java
package com.splitmate.service;

import java.util.List;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;

public interface NotificationService {
    public Notification createNotification(Notification notification);
    public List<Notification> listNotifications(String userId);
    void markAsRead(String notificationId);
}
