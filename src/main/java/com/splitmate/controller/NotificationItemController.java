package com.splitmate.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NotificationItemController {
    @FXML private Label messageLabel;
    @FXML private HBox  buttonBox;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setButtonsVisible(boolean visible) {
        buttonBox.setVisible(visible);
    }

    @FXML
    private void onAccept(ActionEvent e) {
        // TODO: handle accept logic (e.g. call a service)
    }

    @FXML
    private void onDecline(ActionEvent e) {
        // TODO: handle decline logic
    }
}