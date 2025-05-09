package com.splitmate.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import javafx.fxml.Initializable;

import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;
import com.splitmate.service.SessionService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@Component
public class NotificationsPageController implements Initializable {

    @FXML private VBox notificationList;

    @Autowired private NotificationController notificationController;
    @Autowired private SessionService sessionService;
    @Autowired private ConfigurableApplicationContext springContext;  // to let FXMLLoader use Spring

    @Override
    public void initialize(URL loc, ResourceBundle res) {
        String userId = sessionService.getCurrentUser().getId();
        List<Notification> notifs = notificationController.listUnread(userId);
        for (Notification n : notifs) {
            boolean isFriendRequest = n.getType() == NotificationType.FRIEND_REQUEST;
            addNotification(n.getMessage(), isFriendRequest);
        }
    }

    public void addNotification(String message, boolean hasButtons) {
        try {
            // 1) Tell FXMLLoader exactly where to find the FXML
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/NotificationItem.fxml")
            );
            // 2) Let Spring inject @Autowired into the item controller
            loader.setControllerFactory(springContext::getBean);
            HBox item = loader.load();  // now it knows the location!

            // 3) Customize the item
            NotificationItemController ctrl = loader.getController();
            ctrl.setMessage(message);
            ctrl.setButtonsVisible(hasButtons);

            // 4) Add it to the list
            notificationList.getChildren().add(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


