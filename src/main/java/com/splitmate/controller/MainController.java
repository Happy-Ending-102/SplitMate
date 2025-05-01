/* MainController.java */
package com.splitmate.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.splitmate.model.User;
import com.splitmate.config.SpringContext;

@Component
public class MainController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;

    private UserController userController;

    @FXML
    public void initialize() {
        // Grab UserController bean from Spring context
        ApplicationContext ctx = SpringContext.get();
        this.userController = ctx.getBean(UserController.class);
    }

    @FXML
    private void onRegister() {
        User user = new User();
        user.setName(nameField.getText());
        user.setEmail(emailField.getText());
        // call through our UI controller
        User created = userController.registerUser(user, passwordField.getText());
        System.out.println("Registered user: " + created.getId());
        // TODO: show confirmation in UI
    }
}
