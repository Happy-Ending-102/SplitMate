package com.splitmate.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NotificationsPageController {
    @FXML
    private VBox notificationList; // this is the VBox inside the ScrollPane

    public void addNotification(String message, boolean hasButtons) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NotificationItem.fxml"));
            HBox item = loader.load();

            // Controller of NotificationItem.fxml
            NotificationItemController controller = loader.getController();
            controller.setMessage(message);
            controller.setButtonsVisible(hasButtons);

            // Add the loaded notification to the list
            notificationList.getChildren().add(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
