// File: NotificationServiceImpl.java
package com.splitmate.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.splitmate.repository.NotificationRepository;
import com.splitmate.repository.UserRepository;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notifRepo;
    private final UserRepository userRepo;

    public NotificationServiceImpl(NotificationRepository notifRepo,
                                   UserRepository userRepo) {
        this.notifRepo = notifRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void createNotification(String userId, NotificationType type, String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Notification> listNotifications(String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void markAsRead(String notificationId) {
        throw new UnsupportedOperationException();
    }
}
