package com.splitmate.controller;

import org.springframework.stereotype.Component;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.splitmate.service.SessionService;
import com.splitmate.model.User;

@Component
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginErrorLabel;

    private final MainController mainController;
    private final UserController userController;
    private final SessionService sessionService;

    public LoginController(MainController mainController,
                           UserController userController,
                           SessionService sessionService) {
        this.mainController = mainController;
        this.userController = userController;
        this.sessionService = sessionService;
    }

    @FXML
    private void onLogin() {
        // Hide previous error
        loginErrorLabel.setVisible(false);

        String email = emailField.getText().trim().toLowerCase();
        String pwd   = passwordField.getText();

        // Basic validation
        if (email.isEmpty() || pwd.isEmpty()) {
            showError("Please enter both email and password.");
            return;
        }

        // Attempt authentication
        boolean success = userController.login(email, pwd);
        if (success) {
            // Fetch full user and set in session
            User u = userController.findUserByEmail(email);
            sessionService.setCurrentUser(u);

            mainController.showGroupsView();
        } else {
            showError("Invalid email or password.");
        }
    }

    @FXML
    private void onSignUp() {
        mainController.showSignUpView();
    }

    @FXML
    private void onForgotPassword() {
        loginErrorLabel.setVisible(false);

        String email = emailField.getText().trim().toLowerCase();
        
        if (email.isEmpty()) {
            showError("Please enter your email.");
            return;
        }

        boolean sent;
        
        try {
            sent = userController.resetPassword(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("An error occurred while resetting your password. Please try again.");
            return;
        }
        
        // 3) Inform the user
        if (sent) {
            showInfo("Your new password has been sent to your e-mail address.");
        } else {
            showError("No user was found registered with this email address.");
        }

        // TODO: implement forgot-password flow
    }

    private void showError(String message) {
        loginErrorLabel.setText(message);
        loginErrorLabel.setVisible(true);
    }

    private void showInfo(String message){
        loginErrorLabel.setText(message);
        loginErrorLabel.setVisible(true);
    }
}
