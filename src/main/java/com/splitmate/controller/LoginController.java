package com.splitmate.controller;

import org.springframework.stereotype.Component;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Component
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginErrorLabel;

    private final MainController mainController;
    private final UserController userController;

    public LoginController(MainController mainController, UserController userController) {
        this.mainController = mainController;
        this.userController = userController;
    }

    @FXML
    private void onLogin() {
        // Hide previous error
        loginErrorLabel.setVisible(false);

        String email = emailField.getText().trim();
        String pwd   = passwordField.getText();

        // Basic validation
        if (email.isEmpty() || pwd.isEmpty()) {
            showError("Please enter both email and password.");
            return;
        }

        // Attempt authentication
        boolean success = userController.login(email, pwd);
        if (success) {
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
        // TODO: implement forgot-password flow
        System.out.println("Forgot password clicked");
    }

    private void showError(String message) {
        loginErrorLabel.setText(message);
        loginErrorLabel.setVisible(true);
    }
}
