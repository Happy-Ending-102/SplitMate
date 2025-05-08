package com.splitmate.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NotificationItemController {
    @FXML private Label messageLabel;
    @FXML private HBox buttonBox;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setButtonsVisible(boolean visible) {
        buttonBox.setVisible(visible);
    }
}