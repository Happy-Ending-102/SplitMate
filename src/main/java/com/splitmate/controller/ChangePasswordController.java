package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.splitmate.model.User;
import com.splitmate.service.SessionService;
import com.splitmate.service.UserService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

@Component
public class ChangePasswordController implements Initializable {

    @FXML private PasswordField oldPassword;
    @FXML private PasswordField newPassword;
    @FXML private PasswordField newPasswordConfirmation;
    @FXML private Label       errorLabel;

    private final UserService      userService;
    private final PasswordEncoder  passwordEncoder;
    @Autowired private final SessionService   sessionService;
    @Autowired private MainController mainController;

    @Autowired
    public ChangePasswordController(UserService userService,
                                    PasswordEncoder passwordEncoder,
                                    SessionService sessionService) {
        this.userService     = userService;
        this.passwordEncoder = passwordEncoder;
        this.sessionService  = sessionService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }

    @FXML
    void saveNewPassword(ActionEvent event) {
        String oldRaw     = oldPassword.getText().trim();
        String newRaw     = newPassword.getText().trim();
        String confirmRaw = newPasswordConfirmation.getText().trim();

        if (oldRaw.isEmpty() || newRaw.isEmpty() || confirmRaw.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        String userId = sessionService.getCurrentUserId();
        User user     = userService.getUser(userId);
        if (!passwordEncoder.matches(oldRaw, user.getPasswordHash())) {
            showError("Current password is incorrect.");
            return;
        }

        if (!newRaw.equals(confirmRaw)) {
            showError("New password and confirmation do not match.");
            return;
        }

        user.setPasswordHash(passwordEncoder.encode(newRaw));
        userService.updateUser(user);

        errorLabel.setVisible(false);
        mainController.showProfileView();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
