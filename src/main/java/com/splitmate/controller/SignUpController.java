package com.splitmate.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import com.splitmate.model.User;

@Component
public class SignUpController {

    @FXML private TextField surnameField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final UserController userController;
    private final MainController mainController;

    public SignUpController(UserController userController, MainController mainController) {
        this.userController = userController;
        this.mainController = mainController;
    }

    @FXML
    private void onSignUp() {
        User user = new User();
        user.setName(nameField.getText());
        user.setEmail(emailField.getText());

        User created = userController.registerUser(user, passwordField.getText());

        if (created != null) {
            System.out.println("User created: " + created.getId());
            mainController.showLoginView();
        } else {
            showError("Registration failed.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Sign Up Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
