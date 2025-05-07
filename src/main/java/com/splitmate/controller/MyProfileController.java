package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

@Controller
public class MyProfileController implements Initializable{
@FXML
    private Button changePasswordButton;

    @FXML
    private ToggleGroup frequencyToggleGroup;

    @FXML
    private Button saveButton;

    @FXML
    private Text userIdText;

    @FXML
    private ImageView userImage;

    @FXML
    private Text userNameText;

    @FXML
    private Text ibanText;

    @FXML
    void onChangePassword(ActionEvent event) {

    }

    @FXML
    void onSave(ActionEvent event) {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources){
        userIdText.setText("Get from database");
        userNameText.setText("Get from database");
        ibanText.setText("Get from database");
        Image image= new Image("C://Users//LENOVO//Desktop//repositories//SplitMate//src//main//resources//icons");
        userImage.setImage(image);
    }

}