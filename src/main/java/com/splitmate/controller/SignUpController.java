package com.splitmate.controller;

import org.springframework.stereotype.Component;
import com.splitmate.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Component
public class SignUpController {

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label signUpErrorLabel;

    private final UserController userController;
    private final MainController mainController;

    public SignUpController(UserController userController,
                            MainController mainController) {
        this.userController = userController;
        this.mainController = mainController;
    }

    @FXML
    private void onSignUp() {
        // Hide previous error
        signUpErrorLabel.setVisible(false);

        // Gather and trim inputs
        String name    = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email   = emailField.getText().trim();
        String pwd     = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        // Basic validations
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || pwd.isEmpty() || confirm.isEmpty()) {
            showError("All fields are required.");
            return;
        }
        if (!pwd.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }

        // Attempt registration
        try {
            User u = new User();
            // combine name + surname since model only has one 'name' property
            u.setName(name + " " + surname);
            u.setEmail(email);

            userController.registerUser(u, pwd);

            // Success → go to login
            mainController.showLoginView();

        } catch (IllegalArgumentException ex) {
            // This catches "email already registered" and any other validation errors
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        signUpErrorLabel.setText(message);
        signUpErrorLabel.setVisible(true);
    }
}
