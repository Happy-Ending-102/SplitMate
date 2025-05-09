// File: NotificationServiceImpl.java
package com.splitmate.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import com.splitmate.repository.NotificationRepository;
import com.splitmate.repository.UserRepository;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;
import com.splitmate.model.User;

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
    public void createNotification(Notification notification) {
        notifRepo.save(notification);
    }

    @Override
    public List<Notification> listNotifications(String userId) {
        return notifRepo.findAllNotificationsByUserId(userId);
    }

    @Override
    public void markAsRead(String notificationId) {
        Notification notif = notifRepo.findById(notificationId).orElseThrow(() -> new NoSuchElementException("Notification not found"));

        notif.setRead(true);
        notifRepo.save(notif);
    }

}
