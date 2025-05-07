// CurrencyController.java
package com.splitmate.controller;

import java.math.BigDecimal;
import java.util.List;
import com.splitmate.model.*;
import com.splitmate.service.*;
import org.springframework.stereotype.Component;

/**
 * Handles manual currency conversion requests from the UI.
 */
@Component
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public List<Notification> listUnread(String userId) {
        return notificationService.listNotifications(userId);
    }

    public void markAsRead(String notificationId) {
        notificationService.markAsRead(notificationId);
    }
}