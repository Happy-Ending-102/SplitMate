package com.splitmate.controller;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Component
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final MainController mainController;
    private final UserController userController;

    public LoginController(MainController mainController, UserController userController) {
        this.mainController = mainController;
        this.userController = userController;
    }

    @FXML
    private void onLogin() {
        mainController.showGroupsView();
        System.out.println("clicked");
        // String email = emailField.getText();
        // String password = passwordField.getText();

        // if (userController.login(email, password)) {
        //     mainController.showGroupsView();
        // } else {
        //     System.out.println("Login failed");
        // }
    }

    @FXML
    private void onSignUp() {
        mainController.showSignUpView();
    }

    @FXML
    public void onForgotPassword(ActionEvent event) {
        System.out.println("Forgot password clicked");
    }
}