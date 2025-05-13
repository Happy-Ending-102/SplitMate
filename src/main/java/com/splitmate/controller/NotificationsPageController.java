package com.splitmate.controller;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;
import com.splitmate.service.NotificationService;
import com.splitmate.service.SessionService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Controller for the Notifications page. Renders notifications and handles user actions.
 */
@Component
public class NotificationsPageController implements Initializable {

    // Notification types that should display Accept/Decline buttons
    private static final Set<NotificationType> ACTIONABLE = Set.of(
        NotificationType.FRIEND_REQUEST,
        NotificationType.TRANSACTION_RECEIVED
    );

    @FXML private VBox notificationsContainer;

    private final NotificationService notificationService;
    private final SessionService sessionService;
    private final FriendshipController friendshipController;
    private final PaymentController paymentController;

    @Autowired
    public NotificationsPageController(NotificationService notificationService,
                                       SessionService sessionService,
                                       FriendshipController friendshipController, PaymentController paymentController) {
        this.notificationService = notificationService;
        this.sessionService = sessionService;
        this.friendshipController = friendshipController;
        this.paymentController = paymentController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadNotifications();
    }

    private void loadNotifications() {
        notificationsContainer.getChildren().clear();
        String currentUserId = sessionService.getCurrentUser().getId();

        List<Notification> notifications = notificationService
            .listNotifications(currentUserId).stream()
            .filter(n -> !n.isRead())
            .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
            .collect(Collectors.toList());

        for (Notification n : notifications) {
            notificationsContainer.getChildren().add(createCard(n));
        }
    }

    private HBox createCard(Notification notification) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color:rgba(198, 187, 217, 0.63); " +
                      "-fx-background-radius: 10; " +
                      "-fx-padding: 10;");

        Label message = new Label(notification.getMessage());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (ACTIONABLE.contains(notification.getType())) {
            Button acceptBtn = new Button("Accept");
            acceptBtn.setOnAction(evt -> handleAccept(notification));

            Button declineBtn = new Button("Decline");
            declineBtn.setOnAction(evt -> handleDecline(notification));

            card.getChildren().addAll(message, spacer, acceptBtn, declineBtn);
        } else {
            card.getChildren().addAll(message, spacer);
        }

        return card;
    }

    /**
     * Handles acceptance of actionable notifications.
     */
    private void handleAccept(Notification notification) {
        if (notification.getType() == NotificationType.FRIEND_REQUEST) {
            String requesterId = notification.getFriendUser().getId();
            String recipientId = sessionService.getCurrentUser().getId();
            friendshipController.acceptFriendRequest(requesterId, recipientId);
        }
        else if (notification.getType() == NotificationType.TRANSACTION_RECEIVED) {
            String transactionId = notification.getTransaction().getId();
            paymentController.acceptTransaction(transactionId);
        }

        notificationService.markAsRead(notification.getId());
        loadNotifications();
    }

    /**
     * Handles decline action by simply marking the notification as read.
     */
    private void handleDecline(Notification notification) {
        notificationService.markAsRead(notification.getId());
        loadNotifications();
    }
}