package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

@Component
public class ChangePasswordController implements Initializable{

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField newPasswordConfirmation;

    @FXML
    private Button newPasswordSaveButton;

    @FXML
    private PasswordField oldPassword;


    @FXML
    void saveNewPassword(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TO DO 
    }

}
